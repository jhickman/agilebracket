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
