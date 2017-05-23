package com.jx372.mysite.action.board;

import com.jx372.web.action.Action;
import com.jx372.web.action.ActionFactory;

public class BoardActionFactory extends ActionFactory {

	@Override
	public Action getAction(String actionName) {
		Action action = null;
		if( "writeform".equals( actionName ) ) {
			action = new WriteFormAction();
		} else if( "write".equals( actionName ) ) {
			action = new WriteAction();
		} else {
			action = new ListAction();
		}
		
		return action;
	}

}
