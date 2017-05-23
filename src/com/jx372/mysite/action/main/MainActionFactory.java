package com.jx372.mysite.action.main;

import com.jx372.web.action.Action;
import com.jx372.web.action.ActionFactory;

public class MainActionFactory extends ActionFactory {

	@Override
	public Action getAction(String actionName) {
		return new IndexAction();
	}

}
