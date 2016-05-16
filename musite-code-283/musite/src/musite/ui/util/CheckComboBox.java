/**
 * Musite
 * Copyright (C) 2010 Digital Biology Laboratory, University Of Missouri
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package musite.ui.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * ComboBox containing checkbox
 * @author Jianjiong Gao
 */
public class CheckComboBox extends JComboBox {
   private List<ObjCheckBox> cbs;
   private Map<Object, Boolean> mapObjSelected;
   private List<CheckComboBoxSelectionChangedListener> changedListeners
           = new Vector();

   // show all and none option if true
   private boolean showAllAndNone = false;

   private Object nullObject = new Object();

   public CheckComboBox(final Object[] objs) {
       this(Arrays.asList(objs));
   }

   public CheckComboBox(final Object[] objs, final boolean selected) {
       this(Arrays.asList(objs), selected);
   }

   public CheckComboBox(final Object[] objs, final Set selected) {
       this(Arrays.asList(objs), selected);
   }

   public CheckComboBox(final Collection objs) {
       this(objs, false);
   }

   public CheckComboBox(final Collection objs, boolean selected) {
       setObjs(objs, selected);
   }

   public CheckComboBox(final Collection objs, final Set selected) {
       mapObjSelected = new LinkedHashMap();
       for (Object obj : objs) {
           if (obj==null)
               obj = nullObject;
           mapObjSelected.put(obj, selected.contains(obj));
       }

       reset();
   }


   public CheckComboBox(Map<Object, Boolean> mapObjSelected) {
       this.mapObjSelected = mapObjSelected;
       reset();
   }

   public void setShowAllAndNone(boolean showAllAndNone) {
       this.showAllAndNone = showAllAndNone;

       reset();
       repaint();
   }

   public void addSelectionChangedListener (CheckComboBoxSelectionChangedListener l) {
       if (l==null) {
           return;
       }
       changedListeners.add(l);
   }

   public void removeSelectionChangedListener (CheckComboBoxSelectionChangedListener l) {
       changedListeners.remove(l);
   }

   public void setObjs(final Collection objs, boolean selected) {
       mapObjSelected = new LinkedHashMap();
       for (Object obj : objs) {
           mapObjSelected.put(obj, selected);
       }

       reset();
   }

   public List getSelectedItems() {
       List ret = new ArrayList();
       for (Map.Entry<Object,Boolean> entry : mapObjSelected.entrySet()) {
            Object obj = entry.getKey();
            Boolean selected = entry.getValue();

            if (selected) {
                ret.add(obj);
            }
        }

       Collections.sort(ret, new java.util.Comparator(){
            public int compare(Object obj1, Object obj2) {
                return obj1.toString().compareTo(obj2.toString());
            }
        });

        return ret;
   }

   public void addSelectedItems(Collection c) {
       if (c==null) return;

       for (Object obj : c) {
           if (mapObjSelected.containsKey(obj)) {
               mapObjSelected.put(obj, true);
           }
       }

       reset();
       repaint();
   }

   public void addSelectedItems(Object[] objs) {
       if (objs==null) return;

       for (Object obj : objs) {
           if (mapObjSelected.containsKey(obj)) {
               mapObjSelected.put(obj, true);
           }
       }

       reset();
       repaint();
   }

   private void reset() {
       this.removeAllItems();
       
       initCBs();

       this.addItem(new String());

       int n = cbs.size();
       if (!showAllAndNone)
           n -= 2;
       for (int i=0; i<n; i++) {
           JCheckBox cb = cbs.get(i);
           this.addItem(cb);
       }

       setRenderer(new CheckBoxRenderer(cbs));
       addActionListener(this);
   }

   private void initCBs() {
            cbs = new Vector<ObjCheckBox>();

            boolean selectedAll = true;
            boolean selectedNone = true;

            Object[] objs = mapObjSelected.keySet().toArray(new Object[0]);
            java.util.Arrays.sort(objs, new java.util.Comparator(){
                public int compare(Object obj1, Object obj2) {
                    return obj1.toString().compareTo(obj2.toString());
                }
            });

            ObjCheckBox cb;
            for (Object obj : objs) {
                Boolean selected = mapObjSelected.get(obj);

                if (selected) {
                    selectedNone = false;
                } else {
                    selectedAll = false;
                }

                cb = new ObjCheckBox(obj);
                cb.setSelected(selected);
                cbs.add(cb);
            }

            cb = new ObjCheckBox("Select all");
            cb.setSelected(selectedAll);
            cbs.add(cb);

            cb = new ObjCheckBox("Select none");
            cb.setSelected(selectedNone);
            cbs.add(cb);
    }

    private void checkBoxSelectionChanged(int index) {
            int n = cbs.size();
            if (index<0 || index>=n) return;

            //Set selectedObj = getSelected();
            if (index<n-2) {
                ObjCheckBox cb = cbs.get(index);
                if (cb.getObj()==nullObject) {
                    return;
                }

                if (cb.isSelected()) {
                    cb.setSelected(false);
                    mapObjSelected.put(cb.getObj(), false);

                    cbs.get(n-2).setSelected(false); //Select all
                    cbs.get(n-1).setSelected(getSelectedItems()==null);
                } else {
                    cb.setSelected(true);
                    mapObjSelected.put(cb.getObj(), true);

                    List sobjs = getSelectedItems();
                    cbs.get(n-2).setSelected(sobjs!=null && sobjs.size()==n-2); // Select all
                    cbs.get(n-1).setSelected(false);
                }
            } else if (index==n-2) {
                for (Object obj : mapObjSelected.keySet()) {
                    if (obj!=nullObject)
                        mapObjSelected.put(obj, true);
                }

                for (int i=0; i<n-1; i++) {
                    if (cbs.get(i)!=nullObject)
                        cbs.get(i).setSelected(true);
                }
                cbs.get(n-1).setSelected(false);
            } else { // if (index==n-1)
                for (Object obj : mapObjSelected.keySet()) {
                    mapObjSelected.put(obj, false);
                }

                for (int i=0; i<n-1; i++) {
                        cbs.get(i).setSelected(false);
                }
                cbs.get(n-1).setSelected(true);
            }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
            int sel = getSelectedIndex();

            if (sel == 0) {
                    getUI().setPopupVisible(this, false);
            } else if (sel > 0) {
                    checkBoxSelectionChanged(sel-1);
                    for (CheckComboBoxSelectionChangedListener l : changedListeners) {
                        l.selectionChanged(sel-1);
                    }
            }

            this.setSelectedIndex(-1); // clear selection
    }

    @Override
    public void setPopupVisible(boolean flag)
    {
            //TODO this not work, fix it
            // Not code here prevents the populist from closing
    }

    // checkbox renderer for combobox
    class CheckBoxRenderer implements ListCellRenderer {
        private final DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
        private javax.swing.JSeparator separator;
        private final List<ObjCheckBox> cbs;
        //private final Set objs;

        public CheckBoxRenderer(final List<ObjCheckBox> cbs) {
            //setOpaque(true);
            this.cbs = cbs;
            //this.objs = objs;
            separator = new javax.swing.JSeparator(javax.swing.JSeparator.HORIZONTAL);
        }

        //@Override
        public Component getListCellRendererComponent(
                                JList list,
                                Object value,
                                int index,
                                boolean isSelected,
                                boolean cellHasFocus) {          
            if (index > 0 && index <= cbs.size()) {
                    ObjCheckBox cb = cbs.get(index-1);
                    if (cb.getObj()==nullObject) {
                        return separator;
                    }

                    cb.setBackground(isSelected ? Color.blue : Color.white);
                    cb.setForeground(isSelected ? Color.white : Color.black);

                    return cb;
            }

            String str;
            List objs = getSelectedItems();
            if (objs==null || objs.size()==0) {
                str = "Please select";
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(objs.get(0).toString());

                boolean skipped = false;
                for (int i=1; i<objs.size(); i++) {
                    sb.append("; "+objs.get(i));
                    if (sb.length()>40 && i<objs.size()-1) {
                        skipped = true;
                        break;
                    }
                }

                str = sb.toString();
                if (skipped) {
                    str += " ...";
                }
            }
            return defaultRenderer.getListCellRendererComponent(list, str, index, isSelected, cellHasFocus);
        }
    }

    class ObjCheckBox extends JCheckBox {
        private final Object obj;
        public ObjCheckBox(final Object obj) {
            super(obj.toString());
            this.obj = obj;
        }

        public Object getObj() {
            return obj;
        }
    }

}

interface CheckComboBoxSelectionChangedListener extends java.util.EventListener {
    public void selectionChanged(int idx);
}


