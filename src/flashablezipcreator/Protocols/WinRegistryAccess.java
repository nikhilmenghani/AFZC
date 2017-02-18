/*
 * (c) Copyright 2010-2011 AgileBirds
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package flashablezipcreator.Protocols;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

class WinRegistryAccess {

    private static final String REGQUERY_UTIL = "reg query ";

    public static final String REG_SZ_TOKEN = "REG_SZ";

    public static final String REG_BINARY = "REG_BINARY";

    public static final String REG_DWORD_TOKEN = "REG_DWORD";

    /**
     * Returns the value for an attribute of the registry in Windows. If you want to now the processor speed of the machine, you will pass
     * the following path: "HKLM\HARDWARE\DESCRIPTION\System\CentralProcessor\0" and the following attribute name: ~MHz
     * 
     * @param path
     *            - the registry path to the desired value
     * @param attributeName
     *            - the name of the attribute or null for the default
     * @param attributeType
     *            - the type of attribute (DWORD/SZ/...) default is REG_SZ
     * @return - the value for the attribute located in the given path
     */
    public static String getRegistryValue(String path, String attributeName, String attributeType) {
        if (attributeType == null) {
            attributeType = REG_SZ_TOKEN;
        }
        try {
            if (!path.startsWith("\"")) {
                path = "\"" + path + "\"";
            }
            StringBuilder sb = new StringBuilder();
            sb.append(REGQUERY_UTIL);
            sb.append(path);
            sb.append(' ');
            if (attributeName != null) {
                sb.append("/v ");
                sb.append(attributeName);
            } else {
                sb.append("/ve");
            }
            Process process = Runtime.getRuntime().exec(sb.toString());
            ConsoleReader reader = new ConsoleReader(process.getInputStream());
            reader.start();
            process.waitFor();
            reader.join();
            String result = reader.getResult();
            int p = result.indexOf(attributeType);
            if (p == -1) {
                return null;
            }
            return result.substring(p + attributeType.length()).trim();
        } catch (Exception e) {
            return null;
        }
    }

    public static class ConsoleReader extends Thread {
        private InputStream is;

        private StringWriter sw;

        ConsoleReader(InputStream is) {
            this.is = is;
            sw = new StringWriter();
        }

        @Override
        public void run() {
            try {
                int c;
                while ((c = is.read()) != -1) {
                    sw.write(c);
                }
            } catch (IOException e) {
                ;
            }
        }

        String getResult() {
            return sw.toString();
        }
    }

    public static String getJDKHome() {
        String key = "\"HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\Java Development Kit\"";
        String currentVersionAtt = "CurrentVersion";
        String javaHomeAtt = "JavaHome";
        String res1 = getRegistryValue(key, currentVersionAtt, null);
        String res2 = getRegistryValue(key + "\\" + res1, javaHomeAtt, null);
        return res2;
    }

    public static void main(String s[]) {
        String key = "\"HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\Java Development Kit\"";
        String currentVersionAtt = "CurrentVersion";
        String javaHomeAtt = "JavaHome";
        String res1 = getRegistryValue(key, currentVersionAtt, null);
        String res2 = getRegistryValue(key + "\\" + res1, javaHomeAtt, null);
        System.out.println("CurrentVersion '" + res1 + "'");
        System.out.println("JavaHome '" + res2 + "'");
    }
}
