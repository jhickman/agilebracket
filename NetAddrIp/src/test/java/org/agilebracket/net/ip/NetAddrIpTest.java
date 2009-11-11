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

/**
 *
 */
public class NetAddrIpTest
{
    /**
     * FIXME Doesn't work yet.  getting NullPointerException and ArrayIndexOutOfBoundsException
     */
    @Test
    public void testValueOf()
    {
        String[] invalidIpAddresses = new String[]
                {
                        null,
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
    }
}
