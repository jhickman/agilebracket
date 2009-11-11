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

