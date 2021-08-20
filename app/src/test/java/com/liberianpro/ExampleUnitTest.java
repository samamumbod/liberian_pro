package com.liberianpro;

import com.liberian.auth.LiberianAuth;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void generateUUID(){
        UUID uuid = UUID.nameUUIDFromBytes("UBa17E0045".getBytes());
        UUID uuid1 = UUID.nameUUIDFromBytes("UBA17E0044".getBytes());
        assertNotNull(uuid);
        System.out.println(uuid);
        System.out.println(uuid1.toString().length());
    }


    @Test
    public void testUUID(){
        assertNotNull(getUUID("UBa17E0045"));
        System.out.println(getUUID("UBa17E0045"));
    }

    public int getUUID(String regNumber){
        int result=0;
        char[] number = regNumber.toCharArray();
        for ( char n: number) {
            result += n;
        }
        return  result;
    }


}