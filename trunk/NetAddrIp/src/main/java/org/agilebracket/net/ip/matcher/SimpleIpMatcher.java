
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

package org.agilebracket.net.ip.matcher;

import org.agilebracket.net.ip.NetAddrIp;
import org.agilebracket.util.Assert;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class SimpleIpMatcher
    implements IpMatcher
{
    private static final Logger logger = Logger.getLogger(SimpleIpMatcher.class.getName());

    private NetAddrIp[] rejectedIpBlocks = new NetAddrIp[0];
    private NetAddrIp[] acceptedIpBlocks = new NetAddrIp[0];
    private boolean defaultAccept = false;

    public NetAddrIp[] getRejectedIpBlocks()
    {
        return rejectedIpBlocks;
    }
    public void setRejectedIpBlocks(NetAddrIp[] rejectedIpBlocks)
    {
        this.rejectedIpBlocks = rejectedIpBlocks;
        if (logger.isLoggable(Level.FINE))
        {
            for (NetAddrIp ipBlock : rejectedIpBlocks)
            {
                logger.log(Level.FINE, "Setting rejected IP block " + ipBlock);
            }
        }
    }

    public NetAddrIp[] getAcceptedIpBlocks()
    {
        return acceptedIpBlocks;
    }
    public void setAcceptedLocalPorts(NetAddrIp[] acceptedIpBlocks)
    {
        this.acceptedIpBlocks = acceptedIpBlocks;
        if (logger.isLoggable(Level.FINE))
        {
            for (NetAddrIp ipBlock : acceptedIpBlocks)
            {
                logger.log(Level.FINE, "Setting accepted IP block " + ipBlock);
            }
        }
    }

    public boolean isDefaultAccept()
    {
        return defaultAccept;
    }
    public void setDefaultAccept(boolean defaultAccept)
    {
        this.defaultAccept = defaultAccept;
        if (logger.isLoggable(Level.FINE)) logger.log(Level.FINE, "Setting default accept = " + defaultAccept);
    }

    public void afterPropertiesSet()
        throws Exception
    {
        validateAfterInitialization();
    }

    public void validateAfterInitialization()
        throws Exception
    {
        Assert.notNull(rejectedIpBlocks, "A rejectedIpBlocks array is required.");
        Assert.notNull(acceptedIpBlocks, "An acceptedIpBlocks array is required.");
        Assert.isTrue(rejectedIpBlocks.length > 0 || acceptedIpBlocks.length > 0,
                      "Either rejectedIpBlocks or acceptedIpBlocks or both must have an entry.");
    }

    public boolean matches(NetAddrIp netAddrIp)
    {
        if (netAddrIp == null) return false;
        
        for (NetAddrIp rejectedIpBlock : rejectedIpBlocks)
        {
            if (netAddrIp.within(rejectedIpBlock)) return false;
        }

        if (isDefaultAccept()) return true;

        for (NetAddrIp acceptedIpBlock : acceptedIpBlocks)
        {
            if (netAddrIp.within(acceptedIpBlock)) return false;
        }

        return false;
    }
}

