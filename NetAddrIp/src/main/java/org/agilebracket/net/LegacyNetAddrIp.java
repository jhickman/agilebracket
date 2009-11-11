// -*- java -*- @(#)
// Content-Type: text/java

package org.agilebracket.net;

import java.net.Inet4Address;
import java.net.*;

import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;

/**
 * 
 */
public class LegacyNetAddrIp
    implements Comparable<LegacyNetAddrIp>
{
    //------------------------------------------------------------------

    byte[] addr = new byte[4];
    byte[] mask = new byte[4];
    Inet4Address inet4Addr = null;
    Inet4Address inet4Mask = null;
    byte prefixLength = -1;

    final static byte[] ALL_ZEROES = new byte[]
                                { (byte)  0, (byte)  0, (byte)  0, (byte)  0 };
    final static byte[] ALL_ONES   = new byte[]
                                { (byte)255, (byte)255, (byte)255, (byte)255 };
    final static byte[] HOST_MASK = ALL_ONES;

    final static String[] NETMASK_STRINGS = {
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
    
    public final static List<String> NETMASK_STRING_LIST
                = Collections.unmodifiableList(Arrays.asList(NETMASK_STRINGS));

    private final static String[] EMPTY_ARRAY_OF_STRINGS = new String[] { };

    //------------------------------------------------------------------
    // constructors
    //------------------------------------------------------------------
    public LegacyNetAddrIp(byte[] addr, byte[] mask)
        throws java.net.UnknownHostException
    {
        if (addr.length != 4 || mask.length != 4)
        {
            throw new UnknownHostException(
                                "Address and netmask must both be 4 octets.");
        }

        try
        {
            this.inet4Addr = (Inet4Address) InetAddress.getByAddress(addr);
            this.inet4Mask = (Inet4Address) InetAddress.getByAddress(mask);
            this.prefixLength = LegacyNetAddrIp.netmaskToPrefixLength(mask);
        }
        /*
        catch (UnknownHostException e)
        {
            throw new UnknownHostException(e.getMessage());
        }
        */
        catch (ClassCastException cce)
        {
            UnknownHostException uhe = new UnknownHostException(cce.getMessage());
            uhe.initCause(cce);
            throw uhe;
        }

        this.addr = addr;
        this.mask = mask;
    }

    public LegacyNetAddrIp(int[] addr, int[] mask)
        throws java.net.UnknownHostException
    {
        this(
                LegacyNetAddrIp.intToByteArray(addr),
                LegacyNetAddrIp.intToByteArray(mask)
            );
        
    }

    public LegacyNetAddrIp(String addrString, String maskString)
        throws UnknownHostException
    {
        this(
                LegacyNetAddrIp.stringToByteArray(addrString),
                LegacyNetAddrIp.stringToByteArray(maskString)
            );
    }

    public static LegacyNetAddrIp valueOf(String ipAddrString)
    {
        // String[] addrMask = ipAddrString.split("/");
        // FIXME
        return null;
    }

    public String toString()
    {
        return addr() + "/" + mask();
    }

    public byte getPrefixLength()
    {
        try
        {
            return LegacyNetAddrIp.netmaskToPrefixLength(this.mask);
        }
        catch (UnknownHostException e)
        {
            return (byte) -1;
        }
    }

    public long size()
    {
        return 1L << (32 - getPrefixLength());
    }

    public String addr()
    {
        return this.inet4Addr.toString().substring(1);
    }

    public String mask()
    {
        return this.inet4Mask.toString().substring(1);
    }

    //public int compare(Object other)
    
    public int compareTo(LegacyNetAddrIp that)
    {
        for (int octetNumber = 0; octetNumber < 4; octetNumber++)
        {
            if ((this.addr)[octetNumber] < (that.addr)[octetNumber])
            {
                return -1;
            }
            else if ((this.addr)[octetNumber] > (that.addr)[octetNumber])
            {
                return 1;
            }
        }

        return 0;
    }

    public boolean equals(Object other)
    {
        if ( ! (other instanceof LegacyNetAddrIp))
        {
            return false;
        }

        LegacyNetAddrIp that = (LegacyNetAddrIp) other;

        for (int octetNumber = 0; octetNumber < 4; octetNumber++)
        {
            if ((this.addr)[octetNumber] != (that.addr)[octetNumber]
                || (this.mask)[octetNumber] != (that.mask)[octetNumber])
            {
                return false;
            }
        }

        return true;
    }

    public boolean equalsAddr(LegacyNetAddrIp that)
    {
        return this.addr().equals(that.addr());
    }

    public boolean equalsMask(LegacyNetAddrIp that)
    {
        return this.mask().equals(that.mask());
    }

    public LegacyNetAddrIp network()
    {
        return safeNewInstance(bitwiseAnd(addr, mask), mask);
    }

    public LegacyNetAddrIp networkAddr()
    {
        return safeNewInstance(bitwiseAnd(addr, mask), HOST_MASK);
    }

    public LegacyNetAddrIp broadcast()
    {
        return safeNewInstance(
                            bitwiseOr(addr, bitwiseComplement(mask)),
                            mask
                            );
    }

    public LegacyNetAddrIp broadcastAddr()
    {
        return safeNewInstance(
                            bitwiseOr(addr, bitwiseComplement(mask)),
                            HOST_MASK
                            );
    }

    public LegacyNetAddrIp onePastBroadcastAddr()
    {
        LegacyNetAddrIp bcast = broadcastAddr();
        return bcast.forceAdd(1);
    }

    public LegacyNetAddrIp add(long addend)
    {
        byte[] netPart  = bitwiseAnd(addr, mask);
        byte[] hostPart = LegacyNetAddrIp.longToByteArray(addrToLong() + addend);
        hostPart = bitwiseAnd(hostPart, bitwiseComplement(mask));
        return safeNewInstance(bitwiseOr(netPart, hostPart), mask);
    }

    /**
     * Does proper wraparound.
     */
    public LegacyNetAddrIp forceAdd(long addend)
    {
        return safeNewInstance(
                            LegacyNetAddrIp.longToByteArray(addrToLong() + addend),
                            mask
                            );
    }

    public synchronized void forceAddToSelf(long addend)
    {
        addr = LegacyNetAddrIp.longToByteArray(addrToLong() + addend);
        try
        {
            this.inet4Addr = (Inet4Address) InetAddress.getByAddress(addr);
        }
        catch (UnknownHostException e)
        {
        }

        return;
    }


    public LegacyNetAddrIp first()
    {
        return network().add(1);
    }

    public LegacyNetAddrIp last()
    {
        return broadcast().add(-1);
    }

    public boolean contains(LegacyNetAddrIp that)
    {
        if (that == null)
        {
            throw new IllegalArgumentException("Argument to 'contains' must be non-null.");
        }

        if (this.getPrefixLength() == 0)
        {
            return true;
        }
        else if (this.getPrefixLength() > that.getPrefixLength())
        {
            return false;
        }
        else
        {
            return equalsByteArray(
                                    bitwiseAnd(this.addr, this.mask),
                                    bitwiseAnd(that.addr, this.mask)
                                );
        }
    }

    public boolean within(LegacyNetAddrIp that)
    {
        if (that == null)
        {
            throw new IllegalArgumentException("Argument to 'within' must be non-null.");
        }

        return that.contains(this);
    }

    public boolean intersects(LegacyNetAddrIp that)
    {
        return this.contains(that) || that.contains(this);
    }

    public boolean intersects(List/*<LegacyNetAddrIp>*/ netAddrIpList)
    {
        for (Iterator ipListIterator = netAddrIpList.iterator();
                ipListIterator.hasNext(); )
        {
            LegacyNetAddrIp that = (LegacyNetAddrIp) ipListIterator.next();
            if (this.intersects(that)) return true;
        }

        return false;
    }

    //------------------------------------------------------------------
    private long addrToLong()
    {
        return addrToLong(addr);
    }

    private long addrToLong(byte[] anAddr)
    {
        return (
                  ((((long) anAddr[0]) << 24) & 0xFF000000L)
                | ((((long) anAddr[1]) << 16) & 0x00FF0000L)
                | ((((long) anAddr[2]) <<  8) & 0x0000FF00L)
                | ((((long) anAddr[3]) <<  0) & 0x000000FFL)
                );
    }

    private static byte[] longToByteArray(long longValue)
    {
        byte[] addr = new byte[4];
        addr[0] = (byte) ((longValue & 0xFF000000L) >>> 24);
        addr[1] = (byte) ((longValue & 0x00FF0000L) >>> 16);
        addr[2] = (byte) ((longValue & 0x0000FF00L) >>>  8);
        addr[3] = (byte) ((longValue & 0x000000FFL)       );
        return addr;
    }

    private byte[] bitwiseAnd(byte[] param1, byte[] param2)
    {
        byte[] result = new byte[4];
        for (int i = 0; i < 4; i++)
        {
            result[i] = (byte) (param1[i] & param2[i]);
        }

        return result;
    }

    private byte[] bitwiseOr(byte[] param1, byte[] param2)
    {
        byte[] result = new byte[4];
        for (int i = 0; i < 4; i++)
        {
            result[i] = (byte) (param1[i] | param2[i]);
        }

        return result;
    }

    private byte[] bitwiseComplement(byte[] param)
    {
        byte[] result = new byte[4];
        for (int i = 0; i < 4; i++)
        {
            result[i] = (byte) ~(param[i]);
        }

        return result;
    }


    //------------------------------------------------------------------
    // private static methods
    //------------------------------------------------------------------
    private boolean equalsByteArray(byte[] param1, byte[] param2)
    {
        for (int octetNumber = 0; octetNumber < 4; octetNumber++)
        {
            if (param1[octetNumber] != param2[octetNumber])
            {
                return false;
            }
        }

        return true;
    }

    private LegacyNetAddrIp safeNewInstance(byte[] anAddr, byte[] aMask)
    {
        try
        {
            return new LegacyNetAddrIp(anAddr, aMask);
        }
        catch (UnknownHostException e)
        {
            return null;
        }
    }

    private static byte[] intToByteArray(int[] intArray)
    {
        byte[] byteArray = new byte[intArray.length];
        for (int i = 0; i < intArray.length; i++)
        {
            byteArray[i] = (byte) intArray[i];
        }
        return byteArray;
    }

    private static byte[] stringToByteArray(String addrString)
        throws java.net.UnknownHostException
    {
        // String[] stringOctets = new String[4];
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

        byte[] byteArray = new byte[4];
        for (int i = 0; i < 4; i++)
        {
            short octetValue = -1;
            try
            {
                octetValue = Short.parseShort(stringOctets[i]);
            }
            catch (Exception e)
            {
                UnknownHostException uhe = new UnknownHostException(e.getMessage());
                uhe.initCause(e);
                throw uhe;
            }

            if (octetValue < 0 || octetValue > 255)
            {
                throw new UnknownHostException(
                                    "Octet value out of range: " + octetValue);
            }

            byteArray[i] = (byte) octetValue;
        }

        return byteArray;
    }


    private static byte netmaskToPrefixLength(byte[] maskArray)
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

    public static boolean isValidPrefixLength(byte prefixLength)
    {
        return (prefixLength >= 0 && prefixLength <= 32);
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

    public static boolean isValidNetmaskString(String netmaskString)
    {
        return NETMASK_STRING_LIST.contains(netmaskString);
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

        return NETMASK_STRING_LIST
                    .subList(minPrefixLength, maxPrefixLength + 1)
                    .contains(netmaskString);
    }

    public static String getNetmaskStringByPrefixLength(byte prefixLength)
    {
        validatePrefixLength(prefixLength);
        return NETMASK_STRING_LIST.get(prefixLength);
    }

    public static byte getPrefixLengthByNetmaskString(String netmaskString)
    {
        validateNetmaskString(netmaskString);
        int index = NETMASK_STRING_LIST.indexOf(netmaskString);
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
        return NETMASK_STRINGS;
    }

    public static String[] getPossibleNetmaskStrings(
                                                        byte minPrefixLength,
                                                        byte maxPrefixLength
                                                    )
    {
        validatePrefixLength(minPrefixLength);
        validatePrefixLength(maxPrefixLength);

        String[] possible = NETMASK_STRING_LIST
                                .subList(minPrefixLength, maxPrefixLength + 1)
                                .toArray(EMPTY_ARRAY_OF_STRINGS);
        List<String> possibleList = Arrays.asList(possible);
        Collections.reverse(possibleList);
        return possibleList.toArray(EMPTY_ARRAY_OF_STRINGS);
    }

    public static boolean isValidIpAddrString(String ipAddrString)
    {
        try
        {
            validateIpAddrString(ipAddrString);
        }
        catch (IllegalArgumentException e)
        {
            return false;
        }

        return true;
    }

    public static void validateIpAddrString(String ipAddrString)
    {
        getOctetsByIpAddrString(ipAddrString);
        return;
    }

    public static byte[] getOctetsByIpAddrString(String ipAddrString)
    {
        if (ipAddrString == null) return null;
        if (ipAddrString.length() < 7 || ipAddrString.length() > 15)
        {
            throw new IllegalArgumentException(
                                "IP address string not well-formed:"
                                + "  IP address string must be between 7 and 15 characters"
                                );
        }

        String[] octetStrings = ipAddrString.split("\\.");
        if (octetStrings.length != 4)
        {
            throw new IllegalArgumentException(
                                "IP address string not well-formed:"
                                + "  IP address string must contain exactly 3 dots"
                                );
        }

        byte[] octets = new byte[4];
        for (int i = 0; i < 4; i++)
        {
            octets[i] = parseIpAddrOctet(octetStrings[i]);
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

        if (theOctet < 0 || theOctet > 255)
        {
            throw new IllegalArgumentException(
                                "IP address string not well-formed:"
                                + "  each octet must have a value between 0 and 255 (inclusive)"
                                );
        }

        return (byte) theOctet;
    }

    public LegacyNetAddrIp copy()
    {
        try
        {
            return new LegacyNetAddrIp(this.addr(), this.mask());
        }
        catch (UnknownHostException e)
        {
            return null;
        }
    }

    public Object clone()
    {
        return copy();
    }
}
