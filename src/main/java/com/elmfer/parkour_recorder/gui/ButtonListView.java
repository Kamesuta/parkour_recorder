package com.elmfer.parkour_recorder.gui;

import org.lwjgl.opengl.GL11;

import com.elmfer.parkour_recorder.animation.Smoother;
import com.elmfer.parkour_recorder.gui.widgets.Widget;
import com.elmfer.parkour_recorder.render.GraphicsHelper;

public class ButtonListView extends Widget
{
	private Smoother scrollPosition = new Smoother();
	private Viewport viewport;
	
	public void setViewport(Viewport viewport)
	{
		this.viewport = viewport;
	}
	
	public void draw()
	{
		updateModelviewAndViewportState();
		matchWithViewportSizeAndPosition();
		
		int scrollerWidth = 5;
		int smallMargin = GuiStyle.Gui.smallMargin();
		int buttonHeight = GuiStyle.Gui.buttonHeight();
		int listHeight = (buttonHeight + smallMargin) * getChildrenWidgets().size() + 80;
		int scrollMovement = Math.max(0, listHeight - (int)viewport.getHeight());
		
		viewport.pushMatrix(true);
		{
			//Draw buttons
			GL11.glPushMatrix();
			{
				GL11.glTranslated(0, scrollPosition.getValue(), 0);
				
				int i = 0;
				for(Widget child : getChildrenWidgets())
				{
					child.width = viewport.getWidth() - scrollerWidth;
					child.height = buttonHeight;
					child.y = (buttonHeight + smallMargin) * i++;
					child.draw();
				}
			}
			GL11.glPopMatrix();
			
			//Draw scroll tab
			int tabHeight = (int) (((float) viewport.getHeight() / listHeight) * viewport.getHeight());
			int tabTravel = (int) (((float) -scrollPosition.getValue() / scrollMovement) * (viewport.getHeight() - tabHeight));

			UIrender.drawRect(viewport.getWidth() - scrollerWidth, 0, viewport.getWidth(), viewport.getHeight(), 
					GraphicsHelper.getIntColor(0.0f, 0.0f, 0.0f, 0.5f));

			UIrender.drawRect(viewport.getWidth() - scrollerWidth, tabTravel, viewport.getWidth(), 
					tabHeight + tabTravel, 
					GraphicsHelper.getIntColor(0.4f, 0.4f, 0.4f, 0.3f));
		}
		viewport.popMatrix();
	}

	@Override
	public void onCursorMove(float mouseX, float mouseY)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMouseClicked(int button)
	{
	}

	@Override
	public void onMouseReleased(int button)
	{	
	}

	@Override
	public void onKeyPressed(int keyCode)
	{
	}

	@Override
	public void onCharTyped(int charTyped)
	{	
	}

	@Override
	public void onMouseScroll(int scrollAmount)
	{
		if(viewport != null && viewport.isHovered(UIinput.getUICursorX(), UIinput.getUICursorY()))
		{
			int smallMargin = GuiStyle.Gui.smallMargin();
			int buttonHeight = GuiStyle.Gui.buttonHeight();
			int listHeight = (buttonHeight + smallMargin) * getChildrenWidgets().size() + 80;
			double maxScroll = -Math.max(0, listHeight - viewport.getHeight());
			
			scrollPosition.grab(Math.min(0.0, Math.max(scrollPosition.grabbingTo() +  scrollAmount * 0.2, maxScroll)));
		}
	}

	@Override
	public void update(SidedUpdate side)
	{
	}
	
	private void matchWithViewportSizeAndPosition()
	{
		x = viewport.left;
		y = viewport.right;
		width = viewport.getWidth();
		height = viewport.getHeight();
	}
}
