
package org.agilebracket.net.ip.matcher;

import org.agilebracket.net.ip.NetAddrIp;

import org.apache.log4j.Logger;

import org.springframework.util.Assert;

/**
 *
 */
public class SimpleIpMatcher
    implements IpMatcher
{
    private static final Logger log = Logger.getLogger(SimpleIpMatcher.class);

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
        if (log.isDebugEnabled())
        {
            for (NetAddrIp ipBlock : rejectedIpBlocks)
            {
                log.debug("Setting rejected IP block " + ipBlock);
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
        if (log.isDebugEnabled())
        {
            for (NetAddrIp ipBlock : acceptedIpBlocks)
            {
                log.debug("Setting accepted IP block " + ipBlock);
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
        if (log.isDebugEnabled()) log.debug("Setting default accept = " + defaultAccept);
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

