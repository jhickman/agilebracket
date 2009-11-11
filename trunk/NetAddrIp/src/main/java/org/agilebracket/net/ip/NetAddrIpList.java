//-*- java -*- @(#)
//Content-Type: text/x-java; charset=iso-8859-1

/**
 * 
 */
package org.agilebracket.net.ip;

import java.util.List;

/**
 */
public interface NetAddrIpList
    extends List<NetAddrIp>
{
    public NetAddrIp[] toArray();

    public void compactify();
    public NetAddrIpList getCompactIpList();
    
    public NetAddrIpList getComplement();
    public NetAddrIpList getComplement(NetAddrIp universe);
    public NetAddrIpList getComplement(NetAddrIpList universe);
    
    public long getIpCount();
    
    public NetAddrIp getIpBlockContaining(NetAddrIp ipContained);
    
    public List<NetAddrIp> getUnmodifiableList();
}

