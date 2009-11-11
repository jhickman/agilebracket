
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

package org.agilebracket.util;

import org.agilebracket.net.ip.NetAddrIp;

/**
 * Don't want to include dependencies on Spring Framework,
 * so this class will serve the same purpose as Springs Assert class.
 */
public class Assert
{

    public static void notNull(Object obj, String message)
    {
        if (obj == null)
        {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isTrue(boolean expression, String message)
    {
        if ( ! expression )
        {
            throw new IllegalArgumentException(message);    
        }
    }
}
