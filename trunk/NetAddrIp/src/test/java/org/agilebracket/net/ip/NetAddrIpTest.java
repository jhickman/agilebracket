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

package org.agilebracket.net.ip;

import static org.junit.Assert.*;
import org.junit.Test;

public class NetAddrIpTest
{
    private static final String VALID_IP_ADDRESS_STRING = "10.0.0.1/24";
    private static final NetAddrIp NET_ADDR_IP = NetAddrIp.valueOf(VALID_IP_ADDRESS_STRING);


    @Test
    public void testAbnormalValueOf()
    {
        String[] invalidIpAddresses = new String[]
                {
                        "",
                        "adsf",
                        "x.x.x.x",
                        "x.x.x.x/x",
                };
        for(String invalidIpAddressString : invalidIpAddresses)
        {
            try
            {
                NetAddrIp.valueOf(invalidIpAddressString);
                fail("Should have failed on invalid IP Address String '" + invalidIpAddressString + "'");
            }
            catch (IllegalArgumentException e) { /* ignore */ }
        }

        assertNull(NetAddrIp.valueOf(null));
    }

    @Test
    public void addressAsString15()
    {
        assertEquals("010.000.000.001", NET_ADDR_IP.getAddressAsString15());
    }

    @Test
    public void netmaskAsString15()
    {
        assertEquals("255.255.255.000", NET_ADDR_IP.getNetmaskAsString15());
    }
}
