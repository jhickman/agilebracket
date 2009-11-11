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

import static org.agilebracket.net.ip.NetAddrIpConstants.DOTTED_QUAD_MAX_LENGTH;
import static org.agilebracket.net.ip.NetAddrIpConstants.DOTTED_QUAD_MIN_LENGTH;
import static org.agilebracket.net.ip.NetAddrIpConstants.MAX_OCTET_VALUE;
import static org.agilebracket.net.ip.NetAddrIpConstants.MIN_OCTET_VALUE;

/**
 * Utilities for octets
 */
public class OctetUtil
{
    private OctetUtil() { }

    public static byte[] getOctetsByIpAddrString(String ipAddrString)
    {
        if (ipAddrString == null) return null;
        if (ipAddrString.length() < DOTTED_QUAD_MIN_LENGTH
            || ipAddrString.length() > DOTTED_QUAD_MAX_LENGTH)
        {
            throw new IllegalArgumentException(
                                "IP address string not well-formed:"
                                + "  IP address string must be between "
                                + DOTTED_QUAD_MIN_LENGTH + " and " + DOTTED_QUAD_MAX_LENGTH
                                + " characters (inclusive), but found length " + ipAddrString.length() + "." 
                                );
        }
    
        String[] octetStrings = ipAddrString.split("\\.");
        if (octetStrings.length != 4)
        {
            throw new IllegalArgumentException(
                                "IP address string not well-formed:"
                                + "  IP address strALL_ONing must contain exactly 3 dots"
                                );
        }
    
        byte[] octets = new byte[4];
        for (int i = 0; i < 4; i++)
        {
            octets[i] = OctetUtil.parseIpAddrOctet(octetStrings[i]);
        }
    
        return octets;
    }

    public static byte parseIpAddrOctet(String octetString)
    {
        short theOctet = Short.MIN_VALUE;
        try
        {
            theOctet = Short.parseShort(octetString);
        }
        catch (NumberFormatException nfe)
        {
            IllegalArgumentException iae = new IllegalArgumentException(
                                "IP address string not well-formed:"
                                + "  each octet must parse to valid number"
                                );
            iae.initCause(nfe);
            throw iae;
        }
    
        if (theOctet < MIN_OCTET_VALUE || theOctet > MAX_OCTET_VALUE)
        {
            throw new IllegalArgumentException(
                                "IP address string not well-formed:"
                                + "  each octet must have a value between "
                                + MIN_OCTET_VALUE + " and " + MAX_OCTET_VALUE
                                + " (inclusive), but found " + theOctet + "."
                                );
        }
    
        return (byte) theOctet;
    }
    
    static int octetsToInt(byte[] octets)
    {
        return (
                  ((octets[0] << 24) & 0xFF000000)
                | ((octets[1] << 16) & 0x00FF0000)
                | ((octets[2] <<  8) & 0x0000FF00)
                | ((octets[3] <<  0) & 0x000000FF)
                );
        
    }

    static short rawOctetToPositiveOctet(byte rawOctet)
    {
        return (short) (rawOctet & 0xFF);
    }

    static long octetsToLong(byte[] octets)
    {
        return octetsToInt(octets) & 0xFFFFFFFF;
    }
    
    static byte[] intToOctetArray(int intValue)
    {
        byte[] addr = new byte[4];
        addr[0] = (byte) ((intValue & 0xFF000000) >>> 24);
        addr[1] = (byte) ((intValue & 0x00FF0000) >>> 16);
        addr[2] = (byte) ((intValue & 0x0000FF00) >>>  8);
        addr[3] = (byte) ((intValue & 0x000000FF)       );
        return addr;
    }

    static byte[] longToOctetArray(long longValue)
    {
        int intValue = (int) (longValue & 0xFFFFFFFF);
        return intToOctetArray(intValue);
    }

    public static short[] rawOctetsToPositiveOctets(byte[] rawOctets)
    {
        short[] octets = new short[4];
        for (int i = 0; i < 4; i++)
        {
            octets[i] = (short) (rawOctets[i] & 0xFF);
        }

        return octets;
    }
    
    public static short[] intToPositiveOctetArray(int intValue)
    {
        return rawOctetsToPositiveOctets(intToOctetArray(intValue));
    }

    static boolean equalsByteArray(byte[] x, byte[] y)
    {
        if (x == null || y == null) return false;
        if (x.length != y.length) return false;
    
        for (int i = 0; i < x.length; i++)
        {
            if (x[i] != y[i])
            {
                return false;
            }
        }
    
        return true;
    }

    static byte[] octetStringArrayToByteArray(String[] stringOctets)
    {
        byte[] byteArray = new byte[4];
        for (int i = 0; i < byteArray.length; i++)
        {
            short octetValue = -1;
            try
            {
                octetValue = Short.parseShort(stringOctets[i]);
            }
            catch (Exception e)
            {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
    
            if (octetValue < MIN_OCTET_VALUE || octetValue > MAX_OCTET_VALUE)
            {
                throw new IllegalArgumentException(
                                    "Octet value out of range: " + octetValue);
            }
    
            byteArray[i] = (byte) octetValue;
        }
    
        return byteArray;
    }

    static String[] stringToOctetStringArray(String addrString)
    {
        String[] stringOctets = { "", "", "", "" };
        int octetNumber = 0;
        for (int pos = 0; pos < addrString.length(); pos++)
        {
            String character = addrString.substring(pos, pos+1);
            if (character.equals("."))
            {
                octetNumber++;
                continue;
            }
    
            stringOctets[octetNumber] += character;
        }
    
        return stringOctets;
    }

    static byte[] stringToOctetArray(String addrString)
    {
        String[] stringOctets = stringToOctetStringArray(addrString);
        return octetStringArrayToByteArray(stringOctets);
    }

    public static byte[] intArrayToOctetArray(int[] intArray)
    {
        byte[] byteArray = new byte[intArray.length];
        for (int i = 0; i < intArray.length; i++)
        {
            byteArray[i] = (byte) intArray[i];
        }
        return byteArray;
    }

    static byte[] bitwiseAnd(byte[] x, byte[] y)
    {
        byte[] result = new byte[4];
        for (int i = 0; i < 4; i++)
        {
            result[i] = (byte) (x[i] & y[i]);
        }
    
        return result;
    }

    static byte[] bitwiseOr(byte[] x, byte[] y)
    {
        byte[] result = new byte[4];
        for (int i = 0; i < 4; i++)
        {
            result[i] = (byte) (x[i] | y[i]);
        }
    
        return result;
    }

    static byte[] bitwiseComplement(byte[] x)
    {
        byte[] result = new byte[4];
        for (int i = 0; i < 4; i++)
        {
            result[i] = (byte) ~(x[i]);
        }
    
        return result;
    }
}
