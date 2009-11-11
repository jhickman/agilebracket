/*
 * This file is part of AgileBracket.
 *
 * AgileBracket is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AgileBracket is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with AgileBracket.  If not, see <http://www.gnu.org/licenses/>.
 */

//-*- java -*- @(#)
//Content-Type: text/x-java; charset=iso-8859-1

/**
 * 
 */
package org.agilebracket.net.ip;

import java.util.Comparator;

import static org.agilebracket.net.ip.NetAddrIpConstants.ALL_ONES_LONG;

/**
 * Utilities for treating an int as an unsigned 32-bit value.
 */
public class UnsignedInt32Util
{
    public static final int MIN_UNSIGNED_VALUE = 0x0;
    public static final int MAX_UNSIGNED_VALUE = 0xFFFFFFFF;

    private static final int SIGN_BIT_ONE = Integer.MIN_VALUE;  // 0x80000000

    private UnsignedInt32Util() { }
    
    public static long getLong(int uint32Value)
    {
        return uint32Value & ALL_ONES_LONG;
    }

    public static boolean greaterThan(int a, int b)
    {
        return (a ^ SIGN_BIT_ONE) > (b ^ SIGN_BIT_ONE);
    }

    public static boolean greaterThanOrEqual(int a, int b)
    {
        return (a ^ SIGN_BIT_ONE) >= (b ^ SIGN_BIT_ONE);
    }
    
    public static boolean lessThan(int a, int b)
    {
        return (a ^ SIGN_BIT_ONE) < (b ^ SIGN_BIT_ONE);   
    }
    
    public static boolean lessThanOrEqual(int a, int b)
    {
        return (a ^ SIGN_BIT_ONE) <= (b ^ SIGN_BIT_ONE);
    }
    
    public static int getComparison(int a, int b)
    {
        if (a == b) return 0;
        return (greaterThan(a, b)) ? 1 : -1;
    }

    public static class UnsignedInt32Comparator
        implements Comparator<Integer>
    {
        public int compare(Integer theA, Integer theB)
        {
            return getComparison(theA.intValue(), theB.intValue());
        }
    }
}
