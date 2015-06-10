package hr.fer.zemris.java.hw07.layoutmans;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.SizeRequirements;

/**
 * The component added with LEFT constraint should always be placed 
 * on the left or right side depending on components orientation 
 * If a component is “LEFT_TO_RIGHT” oriented, it should be placed along the 
 * left side of the container; otherwise along the right side of the container. 
 * Its preferred width will be respected; its height will be 
 * such that it covers the entire vertical available space (minus insets). 
 * Component added with CENTER constraint shall fill the entire remaining space. 
 * The user does not have to add both components (or even a single one).
 */
public class SimpleLayout implements LayoutManager2 {
	private SizeRequirements[] compHeight;;
	private SizeRequirements totalWidth;
	private SizeRequirements totalHeight;
	private Map<Component, SimpleLayoutPlacement> constraintsMap;
	private SizeRequirements[] compWidth;

	public SimpleLayout() {
		super();
		this.constraintsMap = new HashMap<>();
	}
	
	@Override
	public void addLayoutComponent(String name, Component comp) {
		invalidateLayout(comp.getParent());	// not used
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		constraintsMap.remove(comp);
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
		int[] widthSpans = new int[nComps];
		int[] heightOffsets = new int[nComps];
		int[] heightSpans = new int[nComps];
		int[] widthOffsets = new int[nComps];
		
		boolean ltr = target.getComponentOrientation().isLeftToRight();
		
		Dimension alloc = target.getSize();
		Insets in = target.getInsets();
		alloc.width -= in.left + in.right;
		alloc.height -= in.top + in.bottom;
		
		SizeRequirements.calculateAlignedPositions(alloc.height, totalHeight,
				compHeight, heightOffsets, heightSpans);
		SizeRequirements.calculateTiledPositions(alloc.width, totalWidth,
				compWidth, widthOffsets, widthSpans, ltr);
		
		int centerOffset = 0;
		int nCentered = 0;
		
		for(int i = 0; i < nComps; i++) {
			Component c = target.getComponent(i);
			if(constraintsMap.get(c) == SimpleLayoutPlacement.LEFT) {
				if(ltr) {
					c.setBounds((int) Math.min(
							(long) in.left + centerOffset, Integer.MAX_VALUE),
                        	(int) Math.min(
                        	(long) in.top, Integer.MAX_VALUE),
                        	compWidth[i].preferred, alloc.height);
				} else {
					c.setBounds((int) Math.min(
							(long) target.getWidth() 
							- in.right 
							- centerOffset 
							- compWidth[i].preferred, Integer.MAX_VALUE),
							(int) Math.min(
							(long) in.top, Integer.MAX_VALUE),
							compWidth[i].preferred, alloc.height);
				}
				centerOffset += compWidth[i].preferred;
			} else {
				nCentered++;
			}
		}
		
		if(nCentered == 0) {
			nCentered++;	// / by zero
		}
		int size = (alloc.width - centerOffset) / nCentered;
		for(int i = 0; i < nComps; i++) {
			Component c = target.getComponent(i);
			if(constraintsMap.get(c) == SimpleLayoutPlacement.CENTER) {
				if(ltr) {
					c.setBounds((int) Math.min(
							(long) in.left + centerOffset, Integer.MAX_VALUE),
							(int) Math.min(
									(long) in.top, Integer.MAX_VALUE),
									size, alloc.height);
					centerOffset += size;
				} else {
					c.setBounds((int) Math.min(
							(long) target.getWidth() 
							- in.right
							- centerOffset
							- size,
							Integer.MAX_VALUE),
							(int) Math.min(
							(long) in.top, Integer.MAX_VALUE),
							size, alloc.height);
				}
				size += size;
			}
		}
	}

	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		if(constraints.getClass().equals(SimpleLayoutPlacement.class)) {
			constraintsMap.put(comp, (SimpleLayoutPlacement) constraints);
		} else {
			throw new InvalidParameterException("Invalid constraint type");
		}
		invalidateLayout(comp.getParent());
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
		compWidth= compHeight = null;
	}
	
	private void setSizes(Container parent) {
		int nComps = parent.getComponentCount();
		compHeight = new SizeRequirements[nComps];
		compWidth = new SizeRequirements[nComps];
		
		for(int i = 0; i < nComps; i++) {
			Component c = parent.getComponent(i);
			if(!c.isVisible()) {
				compHeight[i] = new SizeRequirements(0, 0, 0, c.getAlignmentY());
				compWidth[i] = new SizeRequirements(0, 0, 0, c.getAlignmentX());
				continue;
			}
			Dimension min = c.getMinimumSize();
			Dimension max = c.getMaximumSize();
			Dimension pref = c.getPreferredSize();
 
			compHeight[i] = new SizeRequirements(min.height, pref.height,
					max.height, c.getAlignmentY());
			compWidth[i] = new SizeRequirements(min.width, pref.width,
						max.width, c.getAlignmentX());
		}
		
		totalWidth = SizeRequirements.getTiledSizeRequirements(compWidth);
		totalHeight = SizeRequirements.getAlignedSizeRequirements(compHeight);
	}
	
	public enum SimpleLayoutPlacement {
		LEFT,
		CENTER
	}
}
