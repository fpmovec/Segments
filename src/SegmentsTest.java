import org.junit.Assert;
import org.junit.Test;

import java.util.TreeMap;

import static org.junit.Assert.*;

public class SegmentsTest {

    @Test
    public void isIntersect(){
        double[] arr = new double[8];
        arr[0] = 2;
        arr[1] = 2;
        arr[2] = 3;
        arr[3] = 4;
        arr[4] = 1;
        arr[5] = 2;
        arr[6] = 4;
        arr[7] = 1;
        TreeMap<String, SegmentInfo> tree = new TreeMap<>();
        Assert.assertEquals(true, Segments.WhatDoesSheDo(arr, tree));
    }
    @Test
    public void isIntersect1(){
        double[] arr = new double[8];
        arr[0] = 1;
        arr[1] = 2;
        arr[2] = 3;
        arr[3] = 4;
        arr[4] = 1;
        arr[5] = 2;
        arr[6] = 4;
        arr[7] = 1;
        TreeMap<String, SegmentInfo> tree = new TreeMap<>();
        Assert.assertEquals(true, Segments.WhatDoesSheDo(arr, tree));
    }
}