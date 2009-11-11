//-*- java -*- @(#)
//Content-Type: text/x-java; charset=iso-8859-1

package org.agilebracket.net.ip;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Constants useful for netmasks
 */
public class NetmaskConstants
{
    private NetmaskConstants() { }

    final static String[] NETMASK_STRINGS = new String[] {
        "0.0.0.0",         // /0
        "128.0.0.0",
        "192.0.0.0",
        "224.0.0.0",
        "240.0.0.0",       // /4
        "248.0.0.0",
        "252.0.0.0",
        "254.0.0.0",
        "255.0.0.0",       // /8
        "255.128.0.0",
        "255.192.0.0",
        "255.224.0.0",
        "255.240.0.0",     // /12
        "255.248.0.0",
        "255.252.0.0",
        "255.254.0.0",
        "255.255.0.0",     // /16
        "255.255.128.0",
        "255.255.192.0",
        "255.255.224.0",
        "255.255.240.0",   // /20
        "255.255.248.0",
        "255.255.252.0",
        "255.255.254.0",
        "255.255.255.0",   // /24
        "255.255.255.128",
        "255.255.255.192",
        "255.255.255.224",
        "255.255.255.240", // /28
        "255.255.255.248",
        "255.255.255.252",
        "255.255.255.254",
        "255.255.255.255"  // /32
    };

    final static int[] RAW_NETMASKS = new int[] {
        0x00000000,  // 0.0.0.0 = /0
        0x80000000,
        0xC0000000,
        0xE0000000,
        0xF0000000,
        0xF8000000,
        0xFC000000,
        0xFE000000,
        0xFF000000,  // 255.0.0.0 = /8
        0xFF800000,
        0xFFC00000,
        0xFFE00000,
        0xFFF00000,
        0xFFF80000,
        0xFFFC0000,
        0xFFFE0000,
        0xFFFF0000,  // 255.255.0.0 = /16
        0xFFFF8000,
        0xFFFFC000,
        0xFFFFE000,
        0xFFFFF000,
        0xFFFFF800,
        0xFFFFFC00,
        0xFFFFFE00,
        0xFFFFFF00,  // 255.255.255.0 = /24
        0xFFFFFF80,
        0xFFFFFFC0,
        0xFFFFFFE0,
        0xFFFFFFF0,
        0xFFFFFFF8,
        0xFFFFFFFC,
        0xFFFFFFFE,
        0xFFFFFFFF   // 255.255.255.255 = /32
    };

    final static long[] BLOCK_SIZES = {
        1L << 32,  // /0
        1L << 31,
        1L << 30,
        1L << 29,
        1L << 28,
        1L << 27,
        1L << 26,
        1L << 25,
        1L << 24,  // /8
        1L << 23,
        1L << 22,
        1L << 21,
        1L << 20,
        1L << 19,
        1L << 18,
        1L << 17,
        1L << 16,  // /16
        1L << 15,
        1L << 14,
        1L << 13,
        1L << 12,
        1L << 11,
        1L << 10,
        1L <<  9,
        1L <<  8,  // /24
        1L <<  7,
        1L <<  6,
        1L <<  5,
        1L <<  4,
        1L <<  3,
        1L <<  2,
        1L <<  1,
        1L <<  0   // /32
    };
    
    private static final Long[] BLOCK_SIZES_AS_LONG_OBJECT = new Long[33];
    
    static  // initializer
    {
        for (int i = 0; i < BLOCK_SIZES.length; i++)
        {
            BLOCK_SIZES_AS_LONG_OBJECT[i] = new Long(BLOCK_SIZES[i]);
        }
    }

    private static final Integer[] RAW_NETMASKS_AS_INTEGER_OBJECT = new Integer[33];
    
    static  // initializer
    {
        for (int i = 0; i < RAW_NETMASKS.length; i++)
        {
            RAW_NETMASKS_AS_INTEGER_OBJECT[i] = new Integer(RAW_NETMASKS[i]);
        }
    }

    //------------------------------------------------------------------

    public final static List<String> NETMASK_STRING_LIST
        = Collections.unmodifiableList(Arrays.asList(NETMASK_STRINGS));
    
    public final static List<Integer> RAW_NETMASK_LIST
        = Collections.unmodifiableList(Arrays.asList(RAW_NETMASKS_AS_INTEGER_OBJECT));

    public final static List<Long> BLOCK_SIZE_LIST
        = Collections.unmodifiableList(Arrays.asList(BLOCK_SIZES_AS_LONG_OBJECT));

    //------------------------------------------------------------------

    public static final Map<String, Integer> NETMASK_STRING_TO_RAW_NETMASK_MAP
        = getUnmodifiableMapFromLists(NETMASK_STRING_LIST, RAW_NETMASK_LIST);

    public static final Map<Integer, String> RAW_NETMASK_TO_NETMASK_STRING_MAP
        = getUnmodifiableMapFromLists(RAW_NETMASK_LIST, NETMASK_STRING_LIST);

    public static final Map<String, Long> NETMASK_STRING_TO_BLOCK_SIZE_MAP
        = getUnmodifiableMapFromLists(NETMASK_STRING_LIST, BLOCK_SIZE_LIST);

    public static final Map<Long, String> BLOCK_SIZE_TO_NETMASK_STRING_MAP
        = getUnmodifiableMapFromLists(BLOCK_SIZE_LIST, NETMASK_STRING_LIST);

    public static final Map<Long, Integer> BLOCK_SIZE_TO_RAW_NETMASK_MAP
        = getUnmodifiableMapFromLists(BLOCK_SIZE_LIST, RAW_NETMASK_LIST);

    public static final Map<Integer, Long> RAW_NETMASK_TO_BLOCK_SIZE_MAP
        = getUnmodifiableMapFromLists(RAW_NETMASK_LIST, BLOCK_SIZE_LIST);

    //------------------------------------------------------------------

    public final static String CLASS_A_DOTTED_QUAD = NETMASK_STRINGS[ 8];
    public final static String CLASS_B_DOTTED_QUAD = NETMASK_STRINGS[16];
    public final static String CLASS_C_DOTTED_QUAD = NETMASK_STRINGS[24];

    public final static int MIN_PREFIX_LENGTH =  0;
    public final static int MAX_PREFIX_LENGTH = 32;

    final static int[] OCTET_MASKS = new int[] {
        0xFF000000,
        0x00FF0000,
        0x0000FF00,
        0x000000FF 
    };

    public final static long OCTET_0_MASK = OCTET_MASKS[0];
    public final static long OCTET_2_MASK = OCTET_MASKS[1];
    public final static long OCTET_3_MASK = OCTET_MASKS[2];
    public final static long OCTET_4_MASK = OCTET_MASKS[3];
    
    public final static int RAW_HOST_MASK = RAW_NETMASKS[32];

    //------------------------------------------------------------------

    private static <A extends Object, B extends Object> Map<A, B> getUnmodifiableMapFromLists(List<A> a, List<B> b)
    {
        Map<A, B> map = new HashMap<A, B>();
        
        Iterator<A> iterA = a.iterator();
        Iterator<B> iterB = b.iterator();
        
        while(iterA.hasNext() && iterB.hasNext())
        {
            map.put(iterA.next(), iterB.next());
        }
        
        return Collections.unmodifiableMap(map);
    }
    
    static  // initializer
    {
        int netmaskCount = 33;  // 33 different netmasks: prefix lengths 0 through 32
        assert NETMASK_STRINGS.length == netmaskCount;
        assert RAW_NETMASKS.length    == netmaskCount;
        assert BLOCK_SIZES.length     == netmaskCount;
    }
}
