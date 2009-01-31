/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL$
 *  Revision        $LastChangedRevision$
 *  Last Modified:  $LastChangedDate$
 *  Last Change by: $LastChangedBy$
 * -----------------------------------------------------------------------
 *  Copyright (C) 2007 Keith Stribley <devel@thanlwinsoft.org>
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *  
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *  
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 *  MA 02110-1301 USA
 * -----------------------------------------------------------------------
 */
/**
 * 
 */
package org.thanlwinsoft.languagetest.language.test.meta;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.TreeSet;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.thanlwinsoft.languagetest.eclipse.workspace.WorkspaceLanguageManager;
import org.thanlwinsoft.schemas.languagetest.module.ConfigType;
import org.thanlwinsoft.schemas.languagetest.module.DescType;
import org.thanlwinsoft.schemas.languagetest.module.LangType;
import org.thanlwinsoft.schemas.languagetest.module.MetaDataType;
import org.thanlwinsoft.schemas.languagetest.module.TagType;
import org.thanlwinsoft.schemas.languagetest.module.TestItemType;

/**
 * @author keith
 * Class to wrap MetaData nodes
 */
public class MetaNode
{
    public static final String SEPARATOR = "/";
    private MetaNode parent = null;
    private MetaDataType data = null;
    private MetaNode() {}
    public MetaNode(MetaNode parent, MetaDataType data)
    {
        this.parent = parent;
        this.data = data;
    }
    public MetaNode getParent()
    {
        return parent;
    }
    public boolean isOrphan() { return (parent == null); }
    public boolean hasChildren() { return data.sizeOfMetaDataArray() > 0; }
    public MetaNode [] getChildren()
    {
        MetaNode [] nodes = new MetaNode[data.sizeOfMetaDataArray()];
        for (int i = 0; i < data.sizeOfMetaDataArray(); i++)
        {
            nodes[i] = new MetaNode(this, data.getMetaDataArray(i));
        }
        return nodes;
    }
    public void addMetaData(MetaDataType newChild)
    {
        for (MetaDataType md : data.getMetaDataArray())
        {
            if (md.getMetaId().equals(newChild.getMetaId()))
            {
                MetaNode mn = new MetaNode(this, md);
                for (MetaDataType mdChild : newChild.getMetaDataArray())
                {
                    mn.addMetaData(mdChild);
                }
                break;
            }
        }
        data.addNewMetaData().set(newChild.copy());
    }
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) return false;
        if (obj instanceof MetaNode)
        {
            MetaNode mn = (MetaNode)obj;
            if (mn.data.getMetaId().equals(data.getMetaId())) return true;
        }
        else if (obj.toString().equals(data.getMetaId()))
        {
            return true;
        }
        return false;
    }
    public IPath toPath()
    {
        IPath p = null;
        if (parent == null)
            p = new Path(data.getMetaId());
        else
        {
            p = parent.toPath().append(data.getMetaId());
        }
        return p;
    }
    public String getString()
    {
        return data.getMetaId();
    }
    public String getPath()
    {
        return getPath(getDefaultLang());
    }
    public String getPath(String lang)
    {
        if (parent == null)
        {
            return SEPARATOR + getName(lang);
        }
        else
        {
            return parent.getPath(lang) + SEPARATOR + getName(lang);
        }
    }
    public String getName(String lang)
    {
        String name = data.getMetaId();
        for (int i = 0; i < data.sizeOfDescArray(); i++)
        {
            DescType desc = data.getDescArray(i);
            if (desc.getLang().equals(lang))
            {
                name = desc.getStringValue();
                return name;
            }
            // if the prefix is the same, then its probably a good substitute
            int langLength = Math.min(lang.length(), desc.getLang().length());
            if (desc.getLang().startsWith(lang.substring(0, langLength)))
            {
                name = desc.getStringValue();
            }
        }
        return name;
    }
    public String getName()
    {
        return getName(getDefaultLang());
    }
    protected String getDefaultLang()
    {
        String lang = System.getProperty("osgi.nl");
        LangType [] userLangs = WorkspaceLanguageManager.findUserLanguages();
        if (userLangs != null && userLangs.length > 0)
            lang = userLangs[0].getLang();
        if (lang == null)
            lang = "en";
        return lang;
    }
    protected ArrayDeque<MetaNode> getNodeAxis()
    {
        ArrayDeque<MetaNode> nodeAxis = new ArrayDeque<MetaNode>();
        nodeAxis.push(this);
        MetaNode top = this;
        while (top.parent != null)
        {
            top = top.parent;
            nodeAxis.push(top);
        }
        return nodeAxis;
    }
    public boolean isSetOnItem(TestItemType item)
    {
        String ref = toPath().toPortableString();
        for (int i = 0; i < item.sizeOfTagArray(); i++)
        {
            TagType tag = item.getTagArray(i);
            if (tag.getRef().equals(ref))
            {
                return true;
            }
        }
        return false;
    }
    public void setOnItem(TestItemType item, boolean state)
    {
    	if (item == null) // may be null when used in search dialog
    		return;
        String ref = toPath().toPortableString();
        for (int i = 0; i < item.sizeOfTagArray(); i++)
        {
            TagType tag = item.getTagArray(i);
            if (tag.getRef().equals(ref))
            {
                if (state) return; // already set
                else
                {
                    item.removeTag(i);
                    return;
                }
            }
        }
        item.addNewTag().setRef(ref);
    }
    public static MetaNode [] getTopLevelNodes(ConfigType config)
    {
        MetaNode[] nodes = new MetaNode[config.sizeOfMetaDataArray()];
        for (int i = 0; i < config.sizeOfMetaDataArray(); i++)
        {
            MetaDataType md = config.getMetaDataArray(i);
            nodes[i] = new MetaNode(null, md);
        }
        return nodes;
    }
    /**
     * Get the top level meta data nodes as an array from the Config nodes.
     * @param config
     * @return
     */
    public static MetaNode [] getTopLevelNodes(ConfigType [] config)
    {
        Comparator <MetaNode> comparator = new MetaNode().new LeafComparator();
        TreeSet <MetaNode> nodes = new TreeSet<MetaNode>(comparator);
        for (int i = 0; i < config.length; i++)
        {
            if (config[i] != null)
            {
                MetaNode[] configNodes = getTopLevelNodes(config[i]);
                for (int j = 0; j < configNodes.length; j++)
                {
                    if (nodes.contains(configNodes[j]))
                    {
                        MetaNode masterNode = nodes.subSet(configNodes[j], 
                                true, configNodes[j], true).first();
                        for (MetaNode mn : configNodes[j].getChildren())
                        {
                            masterNode.addMetaData(mn.data);
                        }
                    }
                    else
                    {
                        nodes.add(configNodes[j]);
                    }
                }
            }
        }
        return nodes.toArray(new MetaNode[nodes.size()]);
    }
    
    /**
     * Compare MetaNodes at the same level in a tree.  
     * @author keith
     * Note: this comparator comparator imposes orderings that are inconsistent 
     * with MetaNode.equals()." 
     * @param <MetaNode>
     */
    class LeafComparator implements Comparator<MetaNode>
    {
        MetaNode mn;
        public LeafComparator() {};
        /* (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(MetaNode o1, MetaNode o2)
        {
            if (o1 == null)
            {
                if (o2 == null) return 0;
                return -1;
            }
            else if (o2 == null)
            {
                return 1;
            }
            // TODO implement a locale comparator based on descriptions
            return o1.data.getMetaId().compareTo(o2.data.getMetaId());
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj)
        {
            return (obj instanceof LeafComparator);
        }
        
    }

    /**
     * @return
     */
    public String getId()
    {
        return data.getMetaId();
    }
    /**
     * @return
     */
    public MetaDataType getData()
    {
        return data;
    }
}
