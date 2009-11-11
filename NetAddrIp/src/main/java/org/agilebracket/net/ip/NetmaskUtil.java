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

import static org.agilebracket.net.ip.NetAddrIpConstants.EMPTY_ARRAY_OF_STRINGS;
import static org.agilebracket.net.ip.NetmaskConstants.MAX_PREFIX_LENGTH;
import static org.agilebracket.net.ip.NetmaskConstants.MIN_PREFIX_LENGTH;
import static org.agilebracket.net.ip.NetmaskConstants.RAW_NETMASKS;
import static org.agilebracket.net.ip.NetmaskConstants.RAW_NETMASK_LIST;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Utilities for netmasks
 */
public class NetmaskUtil
{
    private NetmaskUtil() { }

    static byte netmaskToPrefixLength(byte[] maskArray)
        throws UnknownHostException
    {
        if (maskArray.length != 4)
        {
            throw new UnknownHostException(
                                "Invalid netmask: netmask must be 4 octets.");
        }
    
        boolean foundZero = false;
        byte prefixLength = 0;
        for (int iterOctet = 0; iterOctet < 4; iterOctet++)
        {
            for (int iterBit = 7; iterBit >= 0; iterBit--)
            {
                // variable "bit" has to be at least type byte,
                // but operations widen to int anyway, so make it int.
                int bit = maskArray[iterOctet] & (0x1 << iterBit);
                if (foundZero && bit != 0x0)
                {
                    throw new UnknownHostException(
                            "Invalid netmask: all one bits must be leftmost.");
                }
                else if (bit != 0x0)
                {
                    prefixLength++;
                }
                else if (bit == 0x0)
                {
                    foundZero = true;
                }
            }
        }
    
        return prefixLength;
    }
    
    public static byte netmaskToPrefixLength(int netmask)
    {
        for (byte i = 0; i <= 32; i++)
        {
            if (netmask == RAW_NETMASKS[i]) return i;
        }
        return -1;
    }

    public static boolean isValidPrefixLength(byte prefixLength)
    {
        return (prefixLength >= MIN_PREFIX_LENGTH && prefixLength <= MAX_PREFIX_LENGTH);
    }

    public static void validatePrefixLength(byte prefixLength)
    {
        if ( ! isValidPrefixLength(prefixLength))
        {
            throw new IllegalArgumentException(
                            "Illegal prefix length: "
                            + "prefix length must be between 0 and 32 (inclusive)."
                            );
        }
    }
    
    public static boolean isValidNetmask(int netmask)
    {
        return RAW_NETMASK_LIST.contains(netmask);
    }
    
    public static void validateNetmask(int netmask)
    {
        if ( ! isValidNetmask(netmask))
        {
            throw new IllegalArgumentException(
                            "Illegal netmask: "
                            + "netmask must be 0 or more ones followed by 0 or more zeroes"
                            );
        }
    }

    public static boolean isValidNetmaskString(String netmaskString)
    {
        return NetmaskConstants.NETMASK_STRING_LIST.contains(netmaskString);
    }

    public static void validateNetmaskString(String netmaskString)
    {
        if ( ! isValidNetmaskString(netmaskString))
        {
            throw new IllegalArgumentException(
                            "Illegal netmask string: "
                            + "netmask string must represent a valid netmask; "
                            + "string must be between 7 and 15 characters"
                            );
        }
    }

    public static boolean isValidNetmaskString(
                                                String netmaskString,
                                                byte minPrefixLength,
                                                byte maxPrefixLength
                                                )
    {
        validatePrefixLength(minPrefixLength);
        validatePrefixLength(maxPrefixLength);
    
        return NetmaskConstants.NETMASK_STRING_LIST
                    .subList(minPrefixLength, maxPrefixLength + 1)
                    .contains(netmaskString);
    }

    public static String getNetmaskStringByPrefixLength(byte prefixLength)
    {
        validatePrefixLength(prefixLength);
        return NetmaskConstants.NETMASK_STRING_LIST.get(prefixLength);
    }

    public static byte getPrefixLengthByNetmaskString(String netmaskString)
    {
        validateNetmaskString(netmaskString);
        int index = NetmaskConstants.NETMASK_STRING_LIST.indexOf(netmaskString);
        if (index == -1)
        {
            throw new IllegalArgumentException(
                            "Netmask string validates but was not found as valid."
                            + "  This should not happen."
                            );
        }
    
        return (byte) index;
    }

    public static String[] getPossibleNetmaskStrings()
    {
        return NetmaskConstants.NETMASK_STRINGS;
    }

    @SuppressWarnings("unchecked")
    public static String[] getPossibleNetmaskStrings(
                                                        byte minPrefixLength,
                                                        byte maxPrefixLength
                                                    )
    {
        validatePrefixLength(minPrefixLength);
        validatePrefixLength(maxPrefixLength);
    
        String[] possible = NetmaskConstants.NETMASK_STRING_LIST
                                .subList(minPrefixLength, maxPrefixLength + 1)
                                .toArray(EMPTY_ARRAY_OF_STRINGS);
        List possibleList = Arrays.asList(possible);
        Collections.reverse(possibleList);
        return (String[]) possibleList.toArray(EMPTY_ARRAY_OF_STRINGS);
    }
}

