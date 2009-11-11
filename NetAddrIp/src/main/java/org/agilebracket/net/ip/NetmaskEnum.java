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

/**
 */
public enum NetmaskEnum
{
    SLASH_0  ( 0),
    SLASH_1  ( 1),
    SLASH_2  ( 2),
    SLASH_3  ( 3),
    SLASH_4  ( 4),
    SLASH_5  ( 5),
    SLASH_6  ( 6),
    SLASH_7  ( 7),
    SLASH_8  ( 8),
    SLASH_9  ( 9),
    SLASH_10 (10),
    SLASH_11 (11),
    SLASH_12 (12),
    SLASH_13 (13),
    SLASH_14 (14),
    SLASH_15 (15),
    SLASH_16 (16),
    SLASH_17 (17),
    SLASH_18 (18),
    SLASH_19 (19),
    SLASH_20 (20),
    SLASH_21 (21),
    SLASH_22 (22),
    SLASH_23 (23),
    SLASH_24 (24),
    SLASH_25 (25),
    SLASH_26 (26),
    SLASH_27 (27),
    SLASH_28 (28),
    SLASH_29 (29),
    SLASH_30 (30),
    SLASH_31 (31),
    SLASH_32 (32);

    byte prefixLength;
    int netmask;

    private NetmaskEnum(int prefixLength)
    {
        this.prefixLength = (byte) prefixLength;
        this.netmask = NetmaskConstants.RAW_NETMASKS[prefixLength];
    }
    
    public byte getPrefixLength()
    {
        return prefixLength;
    }
    
    public int getRawNetmask()
    {
        return netmask;
    }
    
    public static NetmaskEnum getByPrefixLength(byte prefixLength)
    {
        switch (prefixLength)
        {
            case  0 : return SLASH_0;
            case  1 : return SLASH_1;
            case  2 : return SLASH_2;
            case  3 : return SLASH_3;
            case  4 : return SLASH_4;
            case  5 : return SLASH_5;
            case  6 : return SLASH_6;
            case  7 : return SLASH_7;
            case  8 : return SLASH_8;
            case  9 : return SLASH_9;
            case 10 : return SLASH_10;
            case 11 : return SLASH_11;
            case 12 : return SLASH_12;
            case 13 : return SLASH_13;
            case 14 : return SLASH_14;
            case 15 : return SLASH_15;
            case 16 : return SLASH_16;
            case 17 : return SLASH_17;
            case 18 : return SLASH_18;
            case 19 : return SLASH_19;
            case 20 : return SLASH_20;
            case 21 : return SLASH_21;
            case 22 : return SLASH_22;
            case 23 : return SLASH_23;
            case 24 : return SLASH_24;
            case 25 : return SLASH_25;
            case 26 : return SLASH_26;
            case 27 : return SLASH_27;
            case 28 : return SLASH_28;
            case 29 : return SLASH_29;
            case 30 : return SLASH_30;
            case 31 : return SLASH_31;
            case 32 : return SLASH_32;
            default : throw new IllegalArgumentException("Invalid prefix length " + prefixLength);
        }
    }
}

