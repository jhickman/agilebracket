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

package org.agilebracket.net.ip;

import static org.agilebracket.net.ip.NetAddrIpConstants.COMPARATOR_BY_ADDRESS_AND_NETMASK;
import static org.agilebracket.net.ip.NetAddrIpConstants.EMPTY_ARRAY_OF_NET_ADDR_IP;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;

/**
 */
public class NetAddrIpUtil
{
    private NetAddrIpUtil() { }
    
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
        OctetUtil.getOctetsByIpAddrString(ipAddrString);
        return;
    }
    
    public static Inet4Address rawAddressToInet4Address(int address)
    {
        try
        {
            return (Inet4Address) InetAddress.getByAddress(OctetUtil.intToOctetArray(address));
        }
        catch (UnknownHostException e)
        {
            throw new IllegalArgumentException(e);
        }
    }
    
    public static String octetsToString(byte[] octets)
    {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < 4; i++)
        {
            buf.append(octets[i] & 0xFF);
            if (i < 3) buf.append(".");
        }
        return buf.toString();
    }

    public static String octetsToString15(byte[] octets)
    {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < 4; i++)
        {
            // FIXME FIXME - this needs to be "%03d" format
            buf.append(octets[i] & 0xFF);
            if (i < 3) buf.append(".");
        }
        return buf.toString();
    }

    public static String addressToString(int address)
    {
        return octetsToString(OctetUtil.intToOctetArray(address));
    }
    
    public static String addressToString15(int address)
    {
        return octetsToString15(OctetUtil.intToOctetArray(address));
    }

    public static int stringToAddress(String addrString)
    {
        return OctetUtil.octetsToInt(OctetUtil.stringToOctetArray(addrString));
    }
    
    static NetAddrIpList compactIntoList(NetAddrIp... netAddrIps)
    {
        NetAddrIpList newList = new NetAddrIpArrayList();
        for (NetAddrIp netAddrIp : netAddrIps)
        {
            newList.add(netAddrIp);
        }

        Collections.sort(newList, COMPARATOR_BY_ADDRESS_AND_NETMASK);
        NetAddrIp.compactSortedInPlace(newList);
        return newList;
    }
    
    public static NetAddrIp[] compact(NetAddrIp... netAddrIps)
    {
        return compactIntoList(netAddrIps).toArray(EMPTY_ARRAY_OF_NET_ADDR_IP);
    }
    
    public static NetAddrIp[] complement(NetAddrIp universe, NetAddrIp... collection)
    {
        if (universe == null) return null;
        NetAddrIpList compactedCollection = compactIntoList(collection);
        NetAddrIpList collectionWithinUniverse = new NetAddrIpArrayList();
        for (NetAddrIp item : compactedCollection)
        {
            if (universe.contains(item))
            {
                collectionWithinUniverse.add(item);
            }
        }
        NetAddrIpList fullCollection = new NetAddrIpArrayList();
        fullCollection.add(universe.oneBeforeNetworkAddr());
        fullCollection.addAll(collectionWithinUniverse);
        fullCollection.add(universe.onePastBroadcastAddr());
        
        NetAddrIpList complement = new NetAddrIpArrayList();
        
        NetAddrIp prev = null;

      FULL_COLLECTION_LOOP:
        for (NetAddrIp curr : fullCollection)
        {
            if (prev == null)  // first time through
            {
                prev = curr;
                continue FULL_COLLECTION_LOOP;
            }

            NetAddrIp ip = prev.onePastBroadcastAddr();
            complement.addAll(
                Arrays.asList(ip.expandWithinBoundsWithoutIntersect(prev, curr))
            );
            
            prev = curr;
        }
        
        return compact(complement.toArray(EMPTY_ARRAY_OF_NET_ADDR_IP));
    }
    
    public static long getCumulativeNetCounts(NetAddrIp... netAddrIps)
    {
        if (true) throw new UnsupportedOperationException();
        
        long count = 0;
        
        // FIXME - not finished
        NetAddrIp[] compactedNets = compact(netAddrIps);
        for (NetAddrIp net : compactedNets)
        {
            
        }
        
        return count;
    }
    
    public static boolean isAnyIntersecting(NetAddrIp... netAddrIps)
    {
        for (int i = 0; i < netAddrIps.length; i++)
        {
            if (netAddrIps[i] == null) continue;
            for (int j = i + 1; j < netAddrIps.length; j++)
            {
                if (netAddrIps[i].intersects(netAddrIps[j]))
                {
                    return true;
                }
            }
        }
        
        return false;
    }
}
