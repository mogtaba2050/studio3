/**
 * Aptana Studio
 * Copyright (c) 2005-2011 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3 (with exceptions).
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */
package com.aptana.editor.js.formatter.nodes;

import com.aptana.editor.js.parsing.ast.JSNode;
import com.aptana.editor.js.parsing.ast.JSNodeTypes;
import com.aptana.formatter.IFormatterDocument;
import com.aptana.formatter.nodes.FormatterBlockWithBeginNode;

/**
 * JS formatter node for get-property nodes (a.b.c).
 * 
 * @author Shalom Gibly <sgibly@appcelerator.com>
 */
public class FormatterJSGetPropertyNode extends FormatterBlockWithBeginNode
{

	private JSNode propertyNode;
	private boolean hasCommentBefore;

	/**
	 * @param document
	 * @param hasCommentBefore
	 */
	public FormatterJSGetPropertyNode(IFormatterDocument document, JSNode propertyNode, boolean hasCommentBefore)
	{
		super(document);
		this.propertyNode = propertyNode;
		this.hasCommentBefore = hasCommentBefore;
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.formatter.nodes.FormatterBlockNode#isAddingBeginNewLine()
	 */
	@Override
	protected boolean isAddingBeginNewLine()
	{
		if (hasCommentBefore || propertyNode.getSemicolonIncluded())
		{
			return true;
		}
		if (propertyNode.getParent() instanceof JSNode)
		{
			JSNode parent = (JSNode) propertyNode.getParent();
			short parentType = parent.getNodeType();
			boolean addingLine = true;
			switch (parentType)
			{
				case JSNodeTypes.DELETE:
				case JSNodeTypes.PRE_DECREMENT:
				case JSNodeTypes.PRE_INCREMENT:
				case JSNodeTypes.VOID:
				case JSNodeTypes.TYPEOF:
				case JSNodeTypes.RETURN:
				case JSNodeTypes.THROW:
					addingLine = false;
					break;
			}
			if (addingLine && parent.getSemicolonIncluded() && parent.getChild(0) == propertyNode)
			{
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.formatter.nodes.AbstractFormatterNode#shouldConsumePreviousWhiteSpaces()
	 */
	@Override
	public boolean shouldConsumePreviousWhiteSpaces()
	{
		return !isAddingBeginNewLine();
	}

}
