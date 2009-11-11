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

// -*- java -*- @(#)
// Content-Type: text/java

package org.agilebracket.net.ip;

import static org.agilebracket.net.ip.NetAddrIpConstants.ALL_ONES_LONG;
import static org.agilebracket.net.ip.NetAddrIpConstants.EMPTY_ARRAY_OF_NET_ADDR_IP;
import static org.agilebracket.net.ip.NetmaskConstants.BLOCK_SIZES;
import static org.agilebracket.net.ip.NetmaskConstants.RAW_HOST_MASK;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 */
public final class NetAddrIp
    implements Comparable<NetAddrIp>, Serializable, Cloneable
{
    public static final NetAddrIp[] EMPTY_THIS_ARRAY = new NetAddrIp[0];

    static final long serialVersionUID = -7775045667571358446L;

    private int address;
    private int netmask;

    //------------------------------------------------------------------
    
    public NetAddrIp(int address, int netmask)
    {
        NetmaskUtil.validateNetmask(netmask);
        this.address = address;
        this.netmask = netmask;
    }
    
    private NetAddrIp(int address, int netmask, boolean ignored)
    {
        this.address = address;
        this.netmask = netmask;
    }
    
    public NetAddrIp(long address, long netmask)
    {
        // netmask will be validated by (int, int) constructor
        this((int) address, (int) netmask);
    }

    public NetAddrIp(byte[] addr, byte[] mask)
    {
        if (addr.length != 4 || mask.length != 4)
        {
            throw new IllegalArgumentException(
                                "Address and netmask must both be 4 octets.");
        }

        address = OctetUtil.octetsToInt(addr);
        netmask = OctetUtil.octetsToInt(mask);
        NetmaskUtil.validateNetmask(netmask);
    }
    
    public NetAddrIp(int[] addr, int[] mask)
    {
        // netmask will be validated by (byte[], byte[]) constructor
        this(
                OctetUtil.intArrayToOctetArray(addr),
                OctetUtil.intArrayToOctetArray(mask)
            );
    }

    public NetAddrIp(String addrString, String maskString)
    {
        // netmask will be validated by (byte[], byte[]) constructor
        this(
                OctetUtil.stringToOctetArray(addrString),
                OctetUtil.stringToOctetArray(maskString)
            );
    }
    
    public NetAddrIp(String addrString, byte prefixLength)
    {
        // netmask will be validated by (byte[], byte[]) constructor
        this(
             addrString,
             NetmaskUtil.getNetmaskStringByPrefixLength(prefixLength)
             );
    }
    
    public NetAddrIp(String addrString, NetmaskEnum netmaskEnum)
    {
        this(addrString, netmaskEnum.getPrefixLength());
    }
    
    public NetAddrIp(int address, NetmaskEnum netmaskEnum)
    {
        this(address, netmaskEnum.getRawNetmask());
    }

    public static NetAddrIp valueOf(String ipAddrString)
    {
        String[] addrAndMask = ipAddrString.split("/");
        String maskPart = addrAndMask[1];
        if (maskPart.length() == 1 || maskPart.length() == 2)  // prefix length
        {
            byte prefixLength = -1;
            try
            {
                prefixLength = Byte.parseByte(maskPart);
            }
            catch (NumberFormatException e)
            {
                throw new IllegalArgumentException("Cannot parse string '" + ipAddrString + "'", e);
            }
            return new NetAddrIp(addrAndMask[0], prefixLength);
        }
        else if (maskPart.length() >= 7 && maskPart.length() <= 15)  // dotted quad
        {
            return new NetAddrIp(addrAndMask[0], addrAndMask[1]);
        }
        else
        {
            throw new IllegalArgumentException("Cannot parse string '" + ipAddrString + "'");
        }
    }

    /**
     * Creates new instance, performing no validation on the netmask.
     * @param address
     * @param netmask
     * @return NetAddrIp
     */
    public static NetAddrIp newInstance(int address, int netmask)
    {
        return new NetAddrIp(address, netmask, false);
    }

    //------------------------------------------------------------------

    public byte getPrefixLength()
    {
        return NetmaskUtil.netmaskToPrefixLength(netmask);
    }

    public long size()
    {
        return BLOCK_SIZES[getPrefixLength()];
        //return 1L << (32 - getPrefixLength());
    }
    public long getSize() { return size(); }

    public String getAddr()
    {
        return NetAddrIpUtil.addressToString(address);
    }
    
    public String addr()
    {
        return NetAddrIpUtil.addressToString(address);
    }

    public String getMask()
    {
        return NetAddrIpUtil.addressToString(netmask);
    }

    public String mask()
    {
        return NetAddrIpUtil.addressToString(netmask);
    }
    
    public NetmaskEnum getNetmaskEnum()
    {
        return NetmaskEnum.getByPrefixLength(this.getPrefixLength());
    }

    public int compareTo(NetAddrIp that)
    {
        return UnsignedInt32Util.getComparison(this.address, that.address);
    }
    
    /**
     * @deprecated
     */
    @Deprecated
    public int compare(NetAddrIp that)
    {
        return compareTo(that);
    }

    @Override public boolean equals(Object other)
    {
        if ( ! (other instanceof NetAddrIp))
        {
            return false;
        }

        NetAddrIp that = (NetAddrIp) other;
        return (this.address == that.address) && (this.netmask == that.netmask);
    }

    @Override public int hashCode()
    {
        return address ^ netmask;
    }

    public boolean equalsAddr(NetAddrIp that)
    {
        return this.address == that.address;
    }

    public boolean equalsMask(NetAddrIp that)
    {
        return this.netmask == that.netmask;
    }

    public NetAddrIp network()
    {
        return newInstance(address & netmask, netmask);
    }
    public NetAddrIp getNetwork() { return network(); }

    public NetAddrIp networkHostAddress()
    {
        return newInstance(address & netmask, RAW_HOST_MASK);
    }
    public NetAddrIp getNetworkHostAddress() { return networkHostAddress(); }

    public NetAddrIp broadcast()
    {
        return newInstance(address | ~netmask, netmask);
    }
    public NetAddrIp getBroadcast() { return broadcast(); }

    public NetAddrIp broadcastHostAddress()
    {
        return newInstance(address | ~netmask, RAW_HOST_MASK);
    }
    public NetAddrIp getBroadcastHostAddress() { return broadcastHostAddress(); }

    public NetAddrIp onePastBroadcastAddr()
    {
        return newInstance((address | ~netmask) + 1, RAW_HOST_MASK);
    }
    public NetAddrIp getOnePastBroadcastAddr() { return onePastBroadcastAddr(); }
    
    public NetAddrIp oneBeforeNetworkAddr()
    {
        return newInstance((address & netmask) - 1, RAW_HOST_MASK);
    }
    public NetAddrIp getOneBeforeNetworkAddr() { return oneBeforeNetworkAddr(); }

    public NetAddrIp add(int addend)
    {
        int netPart = address & netmask;
        int hostPart = (address + addend) & ~netmask;
        return newInstance(hostPart | netPart, netmask);
    }

    public NetAddrIp add(long addend)
    {
        return add((int) addend);
    }

    public NetAddrIp forceAdd(int addend)
    {
        return newInstance(address + addend, netmask);
    }

    public NetAddrIp forceAdd(long addend)
    {
        return forceAdd((int) addend);
    }

    public NetAddrIp first()
    {
        int netPart = address & netmask;
        int hostPart = (netPart + 1) & ~netmask;
        return newInstance(hostPart | netPart, netmask);
    }
    public NetAddrIp getFirst() { return first(); }

    public NetAddrIp last()
    {
        int netPart = address & netmask;
        int broadcastAddress = address | ~netmask;
        int hostPart = (broadcastAddress - 1) & ~netmask;
        return newInstance(hostPart | netPart, netmask);
    }
    public NetAddrIp getLast() { return last(); }

    public boolean contains(NetAddrIp that)
    {
        if (that == null)
        {
            throw new IllegalArgumentException("Argument to 'contains' must be non-null.");
        }

        if (netmask == 0x0)
        {
            return true;
        }
        else if (UnsignedInt32Util.greaterThan(this.netmask, that.netmask))
        {
            return false;
        }
        else
        {
            // not a typo:  we use this.netmask on both sides
            return (this.address & this.netmask) == (that.address & this.netmask);
        }
    }

    public boolean within(NetAddrIp that)
    {
        if (that == null)
        {
            throw new IllegalArgumentException("Argument to 'within' must be non-null.");
        }

        return that.contains(this);
    }

    public boolean intersects(NetAddrIp that)
    {
        return this.contains(that) || that.contains(this);
    }

    public boolean intersects(NetAddrIp... those)
    {
        for (NetAddrIp that : those)
        {
            if (this.intersects(that)) return true;
        }

        return false;
    }
    
    public boolean intersects(List<NetAddrIp> those)
    {
        for (NetAddrIp that : those)
        {
            if (this.intersects(that)) return true;
        }

        return false;
    }
    
    public byte[] getAddressRawOctets()
    {
        return OctetUtil.intToOctetArray(address);
    }

    public byte[] getNetmaskRawOctets()
    {
        return OctetUtil.intToOctetArray(netmask);
    }
    
    public short[] getAddressOctets()
    {
        return OctetUtil.intToPositiveOctetArray(address);
    }
    
    public short[] getNetmaskOctets()
    {
        return OctetUtil.intToPositiveOctetArray(netmask);
    }
    
    short getAddressOctet(int octetNumber)
    {
        return (short) (getAddressRawOctets()[octetNumber] & 0xFF);
    }

    short getNetmaskOctet(int octetNumber)
    {
        return (short) (getNetmaskRawOctets()[octetNumber] & 0xFF);
    }

    public short getAddressZeroBasedOctet(int octetNumber)
    {
        if (octetNumber < 0 || octetNumber > 3)
        {
            throw new IllegalArgumentException("Octet number must be between 0 and 3 inclusive, but found " + octetNumber);
        }

        return getAddressOctet(octetNumber);
    }

    public short getAddressOneBasedOctet(int oneBasedOctetNumber)
    {
        if (oneBasedOctetNumber < 1 || oneBasedOctetNumber > 4)
        {
            throw new IllegalArgumentException("Octet number must be between 1 and 4 inclusive, but found " + oneBasedOctetNumber);
        }

        return getAddressOctet(oneBasedOctetNumber - 1);
    }

    public short getNetmaskZeroBasedOctet(int octetNumber)
    {
        if (octetNumber < 0 || octetNumber > 3)
        {
            throw new IllegalArgumentException("Octet number must be between 0 and 3 inclusive, but found " + octetNumber);
        }

        return getNetmaskOctet(octetNumber);
    }

    public short getNetmaskOneBasedOctet(int oneBasedOctetNumber)
    {
        if (oneBasedOctetNumber < 1 || oneBasedOctetNumber > 4)
        {
            throw new IllegalArgumentException("Octet number must be between 1 and 4 inclusive, but found " + oneBasedOctetNumber);
        }

        return getNetmaskOctet(oneBasedOctetNumber -1);
    }

    public int getRawAddress()
    {
        return address;
    }
    
    public long getRawAddressAsLong()
    {
        return address & ALL_ONES_LONG;
    }
    
    public int getRawNetmask()
    {
        return netmask;
    }
    
    public long getRawNetmaskAsLong()
    {
        return netmask & ALL_ONES_LONG;
    }

    public Inet4Address getAddressAsInet4Address()
    {
        return NetAddrIpUtil.rawAddressToInet4Address(address);
    }

    public Inet4Address getNetmaskAsInet4Address()
    {
        return NetAddrIpUtil.rawAddressToInet4Address(netmask);
    }
    
    public NetAddrIp[] split(String targetNetmaskString)
    {
        return split(targetNetmaskString, NetAddrIpConstants.DEFAULT_MAX_ARRAY_ELEMENTS);
    }
    
    public NetAddrIp[] split(String targetNetmaskString, int maxElements)
    {
        int targetNetmask = NetAddrIpUtil.stringToAddress(targetNetmaskString);
        NetmaskUtil.validateNetmaskString(targetNetmaskString);
        return split(targetNetmask, maxElements);
    }
    
    public NetAddrIp[] split(int targetNetmask)
    {
        return split(targetNetmask, NetAddrIpConstants.DEFAULT_MAX_ARRAY_ELEMENTS);
    }

    public NetAddrIp[] split(int targetNetmask, int maxElements)
    {
        byte targetPrefixLength = NetmaskUtil.netmaskToPrefixLength(targetNetmask);
        int prefixLengthDiff = targetPrefixLength - getPrefixLength();
        if (prefixLengthDiff <= 0) return new NetAddrIp[] { this.clone() };
        int size = 1 << prefixLengthDiff;
        
        if (size > maxElements)
        {
            throw new IllegalStateException("maxElements specified as " + maxElements + " but size would be " + size);
        }

        int delta = ~targetNetmask + 1;
        int start = address & netmask;
        //int end   = address | ~netmask;
        
        int current = start;
        
        NetAddrIp[] nets = new NetAddrIp[size];
        for (int i = 0; i < size; i++)
        {
            nets[i] = newInstance(current, targetNetmask);
            current += delta;
        }
        
        return nets;
    }

    public Iterator<NetAddrIp> splitIterator(int targetNetmask)
    {
        // NOTE - yes, there is code duplication between this and the 'split' method

        byte targetPrefixLength = NetmaskUtil.netmaskToPrefixLength(targetNetmask);
        int prefixLengthDiff = targetPrefixLength - getPrefixLength();
        if (prefixLengthDiff <= 0)
        {
            return new NetAddrIpAddressIncrementingIterator(address, 0, 1, netmask);
        }
        int size = 1 << prefixLengthDiff;
        int delta = ~targetNetmask + 1;
        int start = address & netmask;
        return new NetAddrIpAddressIncrementingIterator(start, delta, size, targetNetmask);
    }

    public NetAddrIp[] hostEnum()
    {
        // TODO - make more efficient; this is wasteful
        NetAddrIp[] all = enumAll();
        NetAddrIp[] hosts = new NetAddrIp[all.length - 2];
        System.arraycopy(all, 1, hosts, 0, hosts.length);
        return hosts;
    }
    public NetAddrIp[] getHostEnum() { return hostEnum(); }

    public NetAddrIp[] enumAll()
    {
        return split(RAW_HOST_MASK);
    }
    public NetAddrIp[] getEnumAll() { return enumAll(); }

    public byte getPrefixLengthDifference(NetAddrIp that)
    {
        return (byte) (this.getPrefixLength() - that.getPrefixLength());
    }

    @Override public NetAddrIp clone()
    {
        return newInstance(address, netmask);
    }
    
    /**
     * @deprecated
     */
    @Deprecated
    public NetAddrIp copy()
    {
        return clone();
    }

    @Override public String toString()
    {
        return addr() + "/" + mask();
    }
    
    public String toCidrString()
    {
        return addr() + "/" + getPrefixLength();
    }
    public String getCidrString()
    {
        return toCidrString();
    }
    
    public String addressToString15()
    {
        return NetAddrIpUtil.addressToString15(address);
    }
    public String getAddressAsString15()
    {
        return addressToString15();
    }

    /**
     * Gets address with trailed zero octets elided.
     * Example:  18.72.0.0 is returned as "18.72"
     */
    public String getShortAddress()
    {
        // TODO - better implementation
        byte prefixLength = getPrefixLength();
        if (prefixLength > 24) return addr();
        if (prefixLength > 16) return addr().replaceFirst("\\.0$", "");
        if (prefixLength >  8) return addr().replaceFirst("\\.0\\.0$", "");
        return addr().replaceFirst("\\.0\\.0\\.0$", "");
    }
    
    public String getShortCidr()
    {
        return getShortAddress() + "/" + getPrefixLength();
    }
    
    public String netmaskToString15()
    {
        return NetAddrIpUtil.addressToString15(netmask);
    }
    public String getNetmaskAsString15()
    {
        return netmaskToString15();
    }
    
    public NetAddrIp withHostMask()
    {
        return newInstance(address, RAW_HOST_MASK);
    }
    
    public NetAddrIp networkAddr()
    {
        return newInstance(address & netmask, RAW_HOST_MASK);
    }
    
    public NetAddrIp broadcastAddr()
    {
        return newInstance(address | ~netmask, RAW_HOST_MASK);
    }
    
    public NetAddrIp doubleNetwork()
    {
        if (netmask == 0x00000000)  // prefix length 0
        {
            return null;
        }

        return newInstance(address, netmask << 1);
    }
    
    public NetAddrIp halveNetwork()
    {
        if (netmask == RAW_HOST_MASK)  // prefix length 32
        {
            return null;
        }
        
        return newInstance(address, netmask >> 1);
    }
    
    public List<NetAddrIp> expandMaxWithoutIntersectAsList(NetAddrIp... used)
    {
        List<NetAddrIp> nets = new ArrayList<NetAddrIp>();
        NetAddrIp current = this;
        int loops = 32;
        while ( ! current.intersects(used))
        {
            nets.add(current);
            if (current.netmask == 0x00000000) break;
            current = current.doubleNetwork();
            if (current == null) break;
            if (--loops < 0) break;
        }
        
        return nets;
    }
    
    public NetAddrIp[] expandMaxWithoutIntersect(NetAddrIp... used)
    {
        return expandMaxWithoutIntersectAsList(used).toArray(EMPTY_ARRAY_OF_NET_ADDR_IP);
    }
    
    public NetAddrIp getLargestNetworkWithoutIntersect(NetAddrIp... used)
    {
        NetAddrIp[] nets = expandMaxWithoutIntersect(used);
        if (nets == null || nets.length == 0) return null;
        return nets[nets.length - 1];
    }
    
    public NetAddrIp[] expandWithinBoundsWithoutIntersect(NetAddrIp bottomBound, NetAddrIp topBound)
    {
        NetAddrIp net = this.getLargestNetworkWithoutIntersect(bottomBound, topBound);
        if (net == null) return new NetAddrIp[0];
        List<NetAddrIp> nets = new ArrayList<NetAddrIp>();
        nets.add(net);
        
        while ( ! nets.get(nets.size()-1).broadcastAddr().forceAdd(1).equals(topBound.networkAddr()))
        {
            NetAddrIp ip = nets.get(nets.size()-1).broadcastAddr().forceAdd(1);
            NetAddrIp largest = ip.getLargestNetworkWithoutIntersect(bottomBound, topBound);
            if (largest != null) nets.add(largest);
        }
        
        return nets.toArray(EMPTY_ARRAY_OF_NET_ADDR_IP);
    }
    
    public NetAddrIp[] complement(NetAddrIp... collection)
    {
        return NetAddrIpUtil.complement(this, collection);
    }
    
    public List<NetAddrIp> complement(List<NetAddrIp> collection)
    {
        return Arrays.asList(complement(collection.toArray(EMPTY_ARRAY_OF_NET_ADDR_IP)));
    }
    
    /**
     * NOT public!
     */
    static void compactSortedInPlace(List<NetAddrIp> list)
    {
        boolean changed = true;
        
        while (changed)
        {
            changed = false;
            NetAddrIp prev = null;
            NetAddrIp curr = null;
            for (Iterator<NetAddrIp> iter = list.iterator(); iter.hasNext(); )
            {
                prev = curr;
                curr = iter.next();
                if (prev == null) continue;
                
                if (prev.contains(curr))
                {
                    iter.remove();
                    changed = true;
                }
                else if (prev.equalsMask(curr))
                {
                    int prevAddress = prev.getRawAddress();
                    int currAddress = curr.getRawAddress();
                    
                    int newNetmask = prev.getRawNetmask() << 1;
                    
                    if ((prevAddress & newNetmask) == (currAddress & newNetmask))
                    {
                        iter.remove();
                        changed = true;
    
                        if (prevAddress != currAddress)
                        {
                            // yes, we are modifying immutable objects
                            prev.address &= newNetmask;
                            prev.netmask = newNetmask;
                        }
                    }
                }
            }
        }
    }
    
    @SuppressWarnings("unused")
    private NetAddrIp readResolve() throws ObjectStreamException
    {
        return newInstance(address, netmask);
    }

    public static class ByAddress implements Comparator<NetAddrIp>
    {
        @SuppressWarnings("synthetic-access")
        public int compare(NetAddrIp a, NetAddrIp b)
        {
            return UnsignedInt32Util.getComparison(a.address, b.address);
        }
    }

    public static class ByAddressDescending implements Comparator<NetAddrIp>
    {
        @SuppressWarnings("synthetic-access")
        public int compare(NetAddrIp a, NetAddrIp b)
        {
            return UnsignedInt32Util.getComparison(b.address, a.address);
        }
    }

    public static class ByAddressAndSize implements Comparator<NetAddrIp>
    {
        @SuppressWarnings("synthetic-access")
        public int compare(NetAddrIp a, NetAddrIp b)
        {
            int c = UnsignedInt32Util.getComparison(a.address, b.address);
            return (c != 0) ? c : UnsignedInt32Util.getComparison(b.netmask, a.netmask);
        }
    }
}
