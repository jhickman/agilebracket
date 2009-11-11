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

import static org.agilebracket.net.ip.NetAddrIpConstants.EMPTY_ARRAY_OF_NET_ADDR_IP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NetAddrIpArrayList
    extends ArrayList<NetAddrIp>
    implements NetAddrIpList, List<NetAddrIp>
{
    @Override public NetAddrIp[] toArray()
    {
        return this.toArray(EMPTY_ARRAY_OF_NET_ADDR_IP);
    }
    
    public static NetAddrIpList fromArray(NetAddrIp... netAddrIps)
    {
        NetAddrIpList list = new NetAddrIpArrayList();
        list.addAll(Arrays.asList(netAddrIps));
        return list;
    }

    /* (non-Javadoc)
     * @see org.agilebracket.net.ip.NetAddrIpList#compactify()
     */
    public void compactify()
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("NetAddrIpArrayList#compactify auto-generated on Aug 2, 2005");
    }

    public NetAddrIpList getCompactIpList()
    {
        List<NetAddrIp> list = NetAddrIpUtil.compactIntoList(this.toArray());
        NetAddrIpList thisList = new NetAddrIpArrayList();
        thisList.addAll(list);
        return thisList;
    }

    /* (non-Javadoc)
     * @see org.agilebracket.net.ip.NetAddrIpList#getComplement()
     */
    public NetAddrIpList getComplement()
    {
        throw new UnsupportedOperationException("NetAddrIpArrayList#getComplement auto-generated on Aug 2, 2005");
    }

    /* (non-Javadoc)
     * @see org.agilebracket.net.ip.NetAddrIpList#getComplement(org.agilebracket.net.NetAddrIp)
     */
    public NetAddrIpList getComplement(NetAddrIp universe)
    {
        throw new UnsupportedOperationException("NetAddrIpArrayList#getComplement auto-generated on Aug 2, 2005");
    }

    /* (non-Javadoc)
     * @see org.agilebracket.net.ip.NetAddrIpList#getComplement(org.agilebracket.net.NetAddrIpList)
     */
    public NetAddrIpList getComplement(NetAddrIpList universe)
    {
        throw new UnsupportedOperationException("NetAddrIpArrayList#getComplement auto-generated on Aug 2, 2005");
    }

    /* (non-Javadoc)
     * @see org.agilebracket.net.ip.NetAddrIpList#getIpCount()
     */
    public long getIpCount()
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("NetAddrIpArrayList#getIpCount auto-generated on Aug 2, 2005");
    }

    /* (non-Javadoc)
     * @see org.agilebracket.net.ip.NetAddrIpList#getIpBlockContaining(org.agilebracket.net.NetAddrIp)
     */
    public NetAddrIp getIpBlockContaining(NetAddrIp ipContained)
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("NetAddrIpArrayList#getIpBlockContaining auto-generated on Aug 2, 2005");
    }

    public List<NetAddrIp> getUnmodifiableList()
    {
        return Collections.unmodifiableList(this);
    }
}

