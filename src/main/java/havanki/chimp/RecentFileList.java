/*
 * CHIMP 1.1.1 - Cyber Helper Internet Monkey Program
 * Copyright (C) 2001-2015 Bill Havanki
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package havanki.chimp;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.prefs.Preferences;

/**
 * A list of recent files, with a maximum length of 10. Iteration happens in
 * reverse order of addition.
 */
public class RecentFileList implements Iterable<File> {

  private static final int LENGTH = 10;

  private final ArrayList<String> l;

  /**
   * Creates a new empty list.
   */
  public RecentFileList() {
    l = new ArrayList<>();
  }

  /**
   * Adds a file to the list. Iteration will start with this file. If the file
   * is already in the list, it moves to the top.
   */
  public void add(File file) {
    String path = file.getAbsolutePath();
    l.remove(path);
    l.add(0, path);
    limitLength();
  }

  private void limitLength() {
    while (l.size() > LENGTH) {
      l.remove(l.size() - 1);
    }
  }

  /**
   * Clears the list.
   */
  public void clear() {
    l.clear();
  }

  @Override
  public Iterator<File> iterator() {
    ArrayList<File> fileList = new ArrayList<>(l.size());
    for (String path : l) {
      fileList.add(new File(path));
    }
    return Collections.unmodifiableCollection(fileList).iterator();
  }

  private static final String KEY_LEN = "recentLength";
  private static final String KEY_PATH_PREFIX = "recentPath";

  /**
   * Loads a list from user preferences. If not available, an empty list is
   * returned.
   *
   * @return list loaded from preferences
   */
  public static RecentFileList loadFromPreferences() {
    Preferences prefs = Preferences.userNodeForPackage(RecentFileList.class);
    RecentFileList l = new RecentFileList();
    int len = prefs.getInt(KEY_LEN, -1);
    for (int i = len; i >= 0; i--) {
      String path = prefs.get(KEY_PATH_PREFIX + i, null);
      if (path != null) {
        File f = new File(path);
        if (f.exists()) {
          l.add(f);
        }
      }
    }

    return l;
  }

  /**
   * Saves this list to user preferences.
   */
  public void saveToPreferences() {
    Preferences prefs = Preferences.userNodeForPackage(RecentFileList.class);
    for (int i = 1; i <= l.size(); i++) {
      prefs.put(KEY_PATH_PREFIX + i, l.get(i - 1));
    }
    prefs.putInt(KEY_LEN, l.size());
  }

  @Override
  public String toString() {
    return l.toString();
  }
}
