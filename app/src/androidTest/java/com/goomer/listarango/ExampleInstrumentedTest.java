package com.goomer.listarango;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.json.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.goomer.listarango", appContext.getPackageName());
    }

    @Test
    public void deveObterDadosAPI() {
        Context appContext = InstrumentationRegistry.getTargetContext();

        ObtemDadosAPI tester = new ObtemDadosAPI(appContext);

        //se o retorno veio true, foi pq a funcao rodou certinho.. :)
        assertTrue(tester.pegaDados());
    }

    @Test
    public void deveObterRestaurantesMenus() {
        Context appContext = InstrumentationRegistry.getTargetContext();

        ObtemDadosAPI tester = new ObtemDadosAPI(appContext);

        //se o retorno veio true, foi pq a funcao rodou certinho.. :)
        JSONArray lista = tester.pegaDadosRestaurantesMenu();

        //teve retorno válido da api :D
        assertThat("timestamp",
                lista.length(),
                greaterThan(0));
    }


}
