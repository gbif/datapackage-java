/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.frictionlessdata.datapackage;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EncodingTest {

  @Test
  void allTestFilesShouldHaveNoBom() throws Exception {
    File folder = new File("src/test/resources");

    Files.walk(folder.toPath())
        .filter(path -> path.toString().matches(".*\\.(txt|csv|json)$"))
        .forEach(path -> {
          try {
            byte[] bytes = Files.readAllBytes(path);

            boolean hasUtf8Bom = bytes.length >= 3 &&
                (bytes[0] & 0xFF) == 0xEF &&
                (bytes[1] & 0xFF) == 0xBB &&
                (bytes[2] & 0xFF) == 0xBF;

            boolean hasUtf16LEBom = bytes.length >= 2 &&
                (bytes[0] & 0xFF) == 0xFF &&
                (bytes[1] & 0xFF) == 0xFE;

            boolean hasUtf16BEBom = bytes.length >= 2 &&
                (bytes[0] & 0xFF) == 0xFE &&
                (bytes[1] & 0xFF) == 0xFF;

            System.out.printf("File: %s → BOM: %s%n", path,
                hasUtf8Bom ? "UTF-8 BOM" :
                    hasUtf16LEBom ? "UTF-16 LE BOM" :
                        hasUtf16BEBom ? "UTF-16 BE BOM" : "None");

            assertTrue(!hasUtf8Bom && !hasUtf16LEBom && !hasUtf16BEBom,
                "File " + path + " contains a BOM");

          } catch (Exception e) {
            throw new RuntimeException("Failed to check BOM for " + path, e);
          }
        });
  }
}
