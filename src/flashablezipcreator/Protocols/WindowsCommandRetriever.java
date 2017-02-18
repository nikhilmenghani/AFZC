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

import flashablezipcreator.Protocols.WinRegistryAccess.ConsoleReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class WindowsCommandRetriever {
    /**
     * 
     * @param extension
     *            the file extension (with or without the preceding '.')
     * @return the command to execute for the specified <code>extension</code> or null if there are no associated command
     */
    public static String commandForExtension(String extension) {
        String regKey = "HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\FileExts\\" + extension;
        String fileType = WinRegistryAccess.getRegistryValue(regKey, "ProgID", WinRegistryAccess.REG_SZ_TOKEN);
        if (fileType == null) {
            StringBuilder sb = new StringBuilder("cmd /C assoc ");
            sb.append(extension.startsWith(".") ? extension : "." + extension);

            ConsoleReader reader;
            try {
                Process process = Runtime.getRuntime().exec(sb.toString());
                reader = new ConsoleReader(process.getInputStream());
                reader.start();
                process.waitFor();
                reader.join();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            String result = reader.getResult();
            if (result.indexOf("=") > -1) {
                fileType = result.substring(result.indexOf("=") + 1).trim();
            }
        }
        if (fileType == null) {
            return null;
        }
        return getCommandForFileType(fileType);
    }

    public static String getCommandForFileType(String fileType) {
        String path = "HKEY_CLASSES_ROOT\\" + fileType + "\\shell\\open\\command";
        return WinRegistryAccess.getRegistryValue(path, null, WinRegistryAccess.REG_SZ_TOKEN);
    }
}
