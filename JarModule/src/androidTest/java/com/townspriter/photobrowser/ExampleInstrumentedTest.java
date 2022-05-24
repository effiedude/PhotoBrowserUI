package com.townspriter.photobrowser;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import android.content.Context;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest
{
    @Test
    public void useAppContext()
    {
        Context appContext=InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.townspriter.photobrowser.ui",appContext.getPackageName());
    }
}
