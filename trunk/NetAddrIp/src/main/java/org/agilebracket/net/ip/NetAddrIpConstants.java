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
import java.util.regex.Pattern;

/**
 * General useful constants.
 * See also NetmaskConstants.
 */
public class NetAddrIpConstants
{
    private NetAddrIpConstants() { }

    public final static int MIN_OCTET_VALUE =  0x0;   //   0
    public final static int MAX_OCTET_VALUE =  0xFF;  // 255

    public final static int  ALL_ONES_INT    = 0xFFFFFFFF;
    public final static long ALL_ONES_LONG   = 0xFFFFFFFFL;
    public final static int  ALL_ZEROES_INT  = 0x0;
    public final static long ALL_ZEROES_LONG = 0x0L;

    final static byte[] ALL_ZEROES_OCTETS = new byte[]
                                { (byte)  0, (byte)  0, (byte)  0, (byte)  0 };
    final static byte[] ALL_ONES_OCTETS   = new byte[]
                                { (byte)255, (byte)255, (byte)255, (byte)255 };

    final static byte[] HOST_MASK_OCTETS = ALL_ONES_OCTETS;

    final static String[] EMPTY_ARRAY_OF_STRINGS = new String[] { };

    public final static int DOTTED_QUAD_MIN_LENGTH =  7;
    public final static int DOTTED_QUAD_MAX_LENGTH = 15;
    public final static Pattern DOTTED_QUAD_REGEXP = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");

    public final static int IPV4_BITS = 32;
    
    public static final int DEFAULT_MAX_ARRAY_ELEMENTS = 131072;
    
    public static final Comparator<NetAddrIp> COMPARATOR_BY_ADDRESS_ONLY        = new NetAddrIp.ByAddress();
    public static final Comparator<NetAddrIp> COMPARATOR_BY_ADDRESS_ONLY_DESC   = new NetAddrIp.ByAddressDescending();
    public static final Comparator<NetAddrIp> COMPARATOR_BY_ADDRESS_AND_NETMASK = new NetAddrIp.ByAddressAndSize();
    
    static final NetAddrIp[] EMPTY_ARRAY_OF_NET_ADDR_IP = new NetAddrIp[0];
}
