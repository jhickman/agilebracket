
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
