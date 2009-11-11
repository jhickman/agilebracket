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

import java.util.Iterator;

/**
 *
 */
public class NetAddrIpAddressIncrementingIterator
    implements Iterator<NetAddrIp>
{
    private int addressStart;
    private int addressDelta;
    private int count;
    private int netmask;
    private int addressCurrent;
    private int i = 0;

    NetAddrIpAddressIncrementingIterator(int addressStart, int addressDelta, int count, int netmask)
    {
        this.addressStart = addressStart;
        this.addressDelta = addressDelta;
        this.count        = count;
        this.netmask      = netmask;
        this.addressCurrent = this.addressStart;
    }

    public boolean hasNext()
    {
        return (i < count);
    }

    public NetAddrIp next()
    {
        if ( ! hasNext()) return null;

        int address = addressCurrent;

        i++;
        addressCurrent += addressDelta;

        return NetAddrIp.newInstance(address, netmask);
    }

    public void remove()
    {
        throw new UnsupportedOperationException("remove() not meaningful");
    }
}
