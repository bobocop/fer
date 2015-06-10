package hr.fer.zemris.java.hw07.layoutmans;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;

import javax.swing.SizeRequirements;

/**
 * It is a manager that places the components along the vertical axis; 
 * it does not respect their preferred width when doing layout, but instead 
 * it always stretches the components to fill the horizontal area. However, 
 * it does respect their preferred height. The internal public enum defines 
 * the following values: FROM_TOP, FROM_BOTTOM and FILL. In case of FROM_TOP, 
 * components are placed from top of container. In case of FROM_BOTTOM, 
 * components are placed in such a way that the last component is placed at 
 * the bottom of container. In case of FILL, components are stretched so that 
 * they fill entire container.
*/
public class StackedLayout implements LayoutManager2 {
	/*
	 * The instances of this layout manager should not be shared 
	 * among multiple containers because its methods are not
	 * thread-safe.
	 */
	private StackedLayoutDirection direction;
	private SizeRequirements totalWidth;
	private SizeRequirements totalHeight;
	private SizeRequirements[] compWidth;
	private SizeRequirements[] compHeight;

	/**
	 * @param direction an internal enum representing the
	 * placement of items in the layout. See class description.
	 */
	public StackedLayout(StackedLayoutDirection direction) {
		super();
		this.direction = direction;
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
		invalidateLayout(comp.getParent());
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		invalidateLayout(comp.getParent());
	}

	@Override
	public Dimension preferredLayoutSize(Container target) {
		Dimension size;
		setSizes(target);
		size = new Dimension(totalWidth.preferred, totalHeight.preferred);
		Insets insets = target.getInsets();
		size.width = (int) Math.min((long) size.width + (long) insets.left
				+ (long) insets.right, Integer.MAX_VALUE);
		size.height = (int) Math.min((long) size.height + (long) insets.top
				+ (long) insets.bottom, Integer.MAX_VALUE);
		return size;
	}

	@Override
	public Dimension minimumLayoutSize(Container target) {
		Dimension size;
		setSizes(target);
		size = new Dimension(totalWidth.minimum, totalHeight.minimum);
		Insets insets = target.getInsets();
		size.width = (int) Math.min((long) size.width + (long) insets.left
				+ (long) insets.right, Integer.MAX_VALUE);
		size.height = (int) Math.min((long) size.height + (long) insets.top
				+ (long) insets.bottom, Integer.MAX_VALUE);
		return size;
	}

	@Override
	public void layoutContainer(Container target) {
		setSizes(target);
		int nComps = target.getComponentCount();
		int[] widthOffsets = new int[nComps];
		int[] widthSpans = new int[nComps];
		int[] heightOffsets = new int[nComps];
		int[] heightSpans = new int[nComps];
		
		Dimension alloc = target.getSize();
		Insets in = target.getInsets();
		alloc.width -= in.left + in.right;
		alloc.height -= in.top + in.bottom;
		
		SizeRequirements.calculateTiledPositions(alloc.height, totalHeight,
				compHeight, heightOffsets, heightSpans);
		SizeRequirements.calculateAlignedPositions(alloc.width, totalWidth,
				compWidth, widthOffsets, widthSpans);
		
		int offset;
		int heightSpan;
		
		if(direction == StackedLayoutDirection.FROM_BOTTOM) {
			offset = alloc.height - totalHeight.minimum;
		} else {
			offset = 0;
		} 
		
		for(int i = 0; i < nComps; i++) {
			Component c = target.getComponent(i);

			if(direction == StackedLayoutDirection.FILL) {
				heightSpan = heightSpans[i];
			} else {
				heightSpan = compHeight[i].minimum;
			}
			
			c.setBounds((int) Math.min((long) in.left, Integer.MAX_VALUE),
                        (int) Math.min((long) in.top + (long) offset, Integer.MAX_VALUE),
                        widthSpans[i], heightSpan);
			
			offset += c.getHeight();
		}
	}

	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		invalidateLayout(comp.getParent()); // constraints are ignored
	}

	@Override
	public Dimension maximumLayoutSize(Container target) {
		Dimension size;
		setSizes(target);
		size = new Dimension(totalWidth.maximum, totalHeight.maximum);
		Insets insets = target.getInsets();
		size.width = (int) Math.min((long) size.width + (long) insets.left
				+ (long) insets.right, Integer.MAX_VALUE);
		size.height = (int) Math.min((long) size.height + (long) insets.top
				+ (long) insets.bottom, Integer.MAX_VALUE);
		return size;
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		setSizes(target);
		return totalWidth.alignment;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		setSizes(target);
		return totalHeight.alignment;
	}

	@Override
	public void invalidateLayout(Container target) {
		// reset everything
		totalWidth = totalHeight = null;
		compWidth = compHeight = null;
	}
	
	private void setSizes(Container parent) {
		int nComps = parent.getComponentCount();
		compWidth = new SizeRequirements[nComps];
		compHeight = new SizeRequirements[nComps];
		for(int i = 0; i < nComps; i++) {
			Component c = parent.getComponent(i);
			if(!c.isVisible()) {
				compWidth[i] = new SizeRequirements(0, 0, 0, c.getAlignmentX());
				compHeight[i] = new SizeRequirements(0, 0, 0, c.getAlignmentY());
				continue;
			}
			Dimension min = c.getMinimumSize();
			Dimension max = c.getMaximumSize();
			Dimension pref = c.getPreferredSize();
 
			compWidth[i] = new SizeRequirements(min.width, pref.width,
					max.width, c.getAlignmentX());
			compHeight[i] = new SizeRequirements(min.height, pref.height,
					max.height, c.getAlignmentY());
		}
		
		totalWidth = SizeRequirements.getAlignedSizeRequirements(compWidth);
		totalHeight = SizeRequirements.getTiledSizeRequirements(compHeight);

	}

	public enum StackedLayoutDirection {
		FROM_TOP, FROM_BOTTOM, FILL
	}

}
