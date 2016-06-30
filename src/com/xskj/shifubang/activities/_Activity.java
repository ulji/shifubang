package com.xskj.shifubang.activities;

import android.app.Activity;
import android.view.View;

public class _Activity extends Activity {

	@SuppressWarnings("unchecked")
	public <T extends View> T $(int id) {
		return (T) findViewById(id);
	}

}
