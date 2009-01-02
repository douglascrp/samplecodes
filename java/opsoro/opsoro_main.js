/*
 * Copyright (C) 2008 fme AG.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 * As a special exception to the terms and conditions of version 2.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * and Open Source Software ("FLOSS") applications as described in Alfresco's
 * FLOSS exception.  You should have recieved a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * http://www.alfresco.com/legal/licensing
 */
Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
var demoMarkup1 = '<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Sed metus nibh, sodales a, porta at, vulputate eget, dui. Pellentesque ut nisl. Maecenas tortor turpis, interdum non, sodales non, iaculis ac, lacus. Vestibulum auctor, tortor quis iaculis malesuada, libero lectus bibendum purus, sit amet tincidunt quam turpis vel lacus. In pellentesque nisl non sem. Suspendisse nunc sem, pretium eget, cursus a, fringilla vel, urna.';
var demoMarkup2 = '<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Sed metus nibh, sodales a, porta at, vulputate eget, dui. Pellentesque ut nisl. Maecenas tortor turpis, interdum non, sodales non, iaculis ac, lacus. Vestibulum auctor, tortor quis iaculis malesuada, libero lectus bibendum purus, sit amet tincidunt quam turpis vel lacus. In pellentesque nisl non sem. Suspendisse nunc sem, pretium eget, cursus a, fringilla vel, urna.<br/><br/>Aliquam commodo ullamcorper erat. Nullam vel justo in neque porttitor laoreet. Aenean lacus dui, consequat eu, adipiscing eget, nonummy non, nisi. Morbi nunc est, dignissim non, ornare sed, luctus eu, massa. Vivamus eget quam. Vivamus tincidunt diam nec urna. Curabitur velit.</p>';

function viewDocument(url, tabTitle){
    var tagWindow = new Ext.Panel({
        title: tabTitle,
        closable: true,
        layout: 'fit',
        html: '<iframe style="width:100%;height:100%" frameborder = "0"  src="' + ROOT_URL + url + '"></iframe>'
    });
    mainTabPanel.add(tagWindow);
    
}
//propsGrid is global
var propsGrid;
//contentStore is global
var contentStore
Opsoro = function(){
    var viewport;
    var dataStore;
    var previewPanel;
    
    var CropContentRecord = Ext.data.Record.create([{
        name: 'content'
    }, {
        name: 'image'
    }]);
    var currentNodeRef;
    // create reader that reads into cropContent record
    var contentReader = new Ext.data.XmlReader({
        record: "row"
    }, CropContentRecord);
    
    // create the Data Store
    contentStore = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
            url: ALFRESCO_URL + 'service/opsoro/cropContent.xml',
            method: 'GET'
        }),
        
        // let it know about the reader
        reader: contentReader
    });
    var contentTemplate = new Ext.XTemplate('<tpl for=".">', '<div class="preview" id="preview">', '{content}', '<tpl if="this.isImage(image) == true">', '<img src="{image}" width="250"/>', '</tpl>', '</div>', '</tpl>', {
        isImage: function(image){
            return image != '#';
        }
    });
    
    // --------------------------------------------
    // -- Tag Cloud
    // --------------------------------------------
    var tagRecord = Ext.data.Record.create([{
        name: 'name'
    }, {
        name: 'count'
    }]);
    // create reader that reads into cropContent record
    var tagReader = new Ext.data.JsonReader({
        root: "tags",
        id: "name"
    }, tagRecord);
    
    // create the Data Store
    var tagStore = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
            url: ALFRESCO_URL + 'service/opsoro/tagQuery.json',
            method: 'GET'
        }),
        
        // let it know about the reader
        reader: tagReader,
        pruneModifiedRecords: true
    });
    
    var selectedTagStore = new Ext.data.SimpleStore({
        id: 0,
        fields: ['name', 'count']
    
    });
    
    var tagTemplate = new Ext.XTemplate('<div class="cloud"><tpl for=".">', '<span class="tagName{count}"><span class="select">{name}</span><span class="tagCount">({count})</span></span> ', '</tpl></div>');
    
    var tagView = new Ext.DataView({
        store: tagStore,
        tpl: tagTemplate,
        loadingText: 'Loading tags...',
        autoHeight: false,
        multiSelect: true,
        simpleSelect: true,
        itemSelector: 'span.select'
    });
    
    var selectedTagView = new Ext.DataView({
        store: selectedTagStore,
        tpl: new Ext.XTemplate('<div class="tagCloud"><tpl for=".">', '<span class="tagSelected">{name}</span> ', '</tpl></div>'),
        loadingText: 'Loading tags...',
        autoHeight: false,
        // selectedClass : 'tagSelected',
        multiSelect: true,
        simpleSelect: true,
        itemSelector: 'span.tagSelected'
    });
    
    selectedTagView.on("click", function(vw, index, node, e){
        if (node.tagName == 'SPAN') {
            selectedTagStore.remove(selectedTagStore.getAt(index));
            updateTags();
        }
        
    });
    tagView.on("click", function(vw, index, node, e){
        if (node.tagName == 'SPAN') {
            var tagName = node.firstChild.nodeValue;
            // wenn der tag im selectedTagStore vorhanden ist, dann daraus
            // löschen, ansonsten hinzufügen
            if (selectedTagStore.getById(tagName) != null) {
                selectedTagStore.remove(tagStore.getById(tagName));
            }
            else {
                selectedTagStore.add(tagStore.getById(tagName));
            }
            var count = selectedTagStore.getCount();
            updateTags();
        }
    });
    
    function updateTags(){
        var count = selectedTagStore.getCount();
        var tagNames = '';
        for (var i = 0; i < count; i++) {
            var record = selectedTagStore.getAt(i);
            tagNames = tagNames + record.id + '//';
        }
        
        if (tagNames != '') {
            dataStore.load({
                params: {
                    tag: tagNames,
                    start: 0
                }
            });
        }
        else {
            dataStore.removeAll();
        }
        tagStore.load({
            params: {
                tag: tagNames
            }
        });
    }
    
    return {
        initLayout: function(){
            // --------------------------------------------
            // -- LAYOUT
            // --------------------------------------------
            // create some portlet tools using built in Ext tool ids
            var tools = [{
                id: 'gear',
                handler: function(){
                    Ext.Msg.alert('Message', 'The Settings tool was clicked.');
                }
            }, {
                id: 'close',
                handler: function(e, target, panel){
                    panel.ownerCt.remove(panel, true);
                }
            }];
            mainTabPanel = new Ext.TabPanel({
                region: 'center',
                id: 'mainTabPanel',
                deferredRender: false,
                activeTab: 0,
                items: [{
                    el: 'document-grid',
                    title: 'Documents',
                    autoScroll: true
                }, {
                    xtype: 'portal',
                    title: 'My Alfresco',
                    autoScroll: true,
                    margins: '35 5 5 0',
                    items: [{
                        columnWidth: .5,
                        style: 'padding:10px 0 10px 10px',
                        items: [{
                            title: 'My Files',
                            layout: 'fit',
                            tools: tools,
                            items: new MyAlfrescoGrid([0, 2, 3], ALFRESCO_URL + 'service/opsoro/myFiles.json')
                        }, {
                            title: 'My Working Copies',
                            tools: tools,
                            layout: 'fit',
                            items: new MyAlfrescoGrid([0, 1], ALFRESCO_URL + 'service/opsoro/myWorkingCopies.json')
                        }]
                    }, {
                        columnWidth: .5,
                        style: 'padding:10px',
                        items: [{
                            title: 'Panel 3',
                            tools: tools,
                            html: demoMarkup2
                        }, {
                            title: 'My tasks',
                            tools: tools,
                            height: 310,
                            html: '<iframe style="width:100%;height:100%" frameborder = "0"  src="' +
                            ALFRESCO_URL +
                            'service/aggadget/tasks"></iframe>'
                        }]
                    }]
                }]
            
            });
            
            viewport = new Ext.Viewport({
                layout: 'border',
                items: [new Ext.BoxComponent({ // raw
                    region: 'north',
                    el: 'north',
                    height: 50
                }), {
                    region: 'south',
                    contentEl: 'south',
                    split: true,
                    height: 80,
                    minSize: 80,
                    maxSize: 150,
                    collapsible: false,
                    title: 'Message Bar',
                    margins: '0 0 0 0'
                }, {
                    region: 'east',
                    title: 'Informations',
                    collapsible: true,
                    split: true,
                    width: 225,
                    minSize: 175,
                    maxSize: 400,
                    layout: 'fit',
                    margins: '0 5 0 0',
                    layout: 'accordion',
                    layoutConfig: {
                        animate: true
                    },
                    items: [propsGrid = new Ext.grid.PropertyGrid({
                        title: 'Properties', 
						source: {
							"name" : "value", 
							"name2" : "value2"
						}
                    }), previewPanel = new Ext.Panel({
                        collapsible: true,
                        id: 'previewPanel',
                        title: 'Preview',
                        autoScroll: true,
                        items: new Ext.DataView({
                            store: contentStore,
                            tpl: contentTemplate,
                            loadingText: 'Loading...',
                            autoHeight: false,
                            multiSelect: false,
                            itemSelector: 'div.preview',
                            emptyText: 'No preview'
                        })
                    }), {
                        contentEl: 'rules',
                        title: 'Space Rules',
                        autoScroll: true
                    }]
                
                }, {
                    region: 'west',
                    id: 'west-panel',
                    title: 'Navigation',
                    split: true,
                    width: 200,
                    minSize: 175,
                    maxSize: 400,
                    collapsible: true,
                    margins: '0 0 0 5',
                    layout: 'accordion',
                    
                    layoutConfig: {
                        animate: true
                    },
                    items: [{
                        el: 'tree-div',
                        title: 'Folder browsing',
                        border: false,
                        iconCls: 'nav'
                    }, {
                        title: 'Category browsing',
                        el: 'category-div',
                        border: false,
                        iconCls: 'settings'
                    }, {
                        title: 'Tag browsing',
                        el: 'tagCloud',
                        border: false,
                        iconCls: 'tag',
                        items: [selectedTagView, tagView]
                    
                    }]
                }, mainTabPanel]
            });
        },
        initFolderTree: function(){
            // --------------------------------------------
            // -- NAVIGATION TREE
            // --------------------------------------------
            
            var Tree = Ext.tree;
            
            var tree = new Tree.TreePanel({
                el: 'tree-div',
                animate: true,
                loader: new Tree.TreeLoader({
                    dataUrl: ALFRESCO_URL + 'service/opsoro/tree.json',
                    // dataUrl : 'demoData/tree.json',
                    requestMethod: 'GET'
                }),
                containerScroll: true,
                ddGroup: 'GridDD',
                enableDD: true,
                enableDrop: true,
                dropConfig: {
                    appendOnly: true
                }
            });
            
            // set the root node
            var root = new Tree.AsyncTreeNode({
                text: 'Company Home',
                allowDrop: true,
                id: "Home"
            });
            tree.setRootNode(root);
            tree.render();
            
            tree.on('click', showDocumentFromTree);
            function showDocumentFromTree(node){
                if (tree.getSelectionModel().isSelected(node)) {
                    dataStore.baseParams = {
                        node: node.id,
                        limit: 10
                    }
                    dataStore.load({
                        params: {
                            start: 0
                        }
                    });
                }
            };
            
            tree.on('nodedrop', function(e){
                alert('in nodedrop'); // never display this!!!
            });
            
            tree.on('beforenodedrop', function(e){
                alert('in before node drop:' + e.target.id); // This alert
                // msg is
                // getting
                // displayed
            });
        },
        initTagCloud: function(){
        
            tagStore.load();
            
        },
        initCategoryTree: function(){
            // --------------------------------------------
            // -- NAVIGATION TREE
            // --------------------------------------------
            
            var Tree = Ext.tree;
            
            var tree = new Tree.TreePanel({
                el: 'category-div',
                animate: true,
                loader: new Tree.TreeLoader({
                    dataUrl: ALFRESCO_URL + 'service/opsoro/categories.json',
                    // dataUrl : 'demoData/category.json',
                    requestMethod: 'GET'
                }),
                containerScroll: true,
                enableDD: false
            
            });
            
            // set the root node
            var root = new Tree.AsyncTreeNode({
                text: 'Categories',
                id: "categories"
            });
            tree.setRootNode(root);
            tree.render();
            
            tree.on('click', showDocumentFromTree);
            function showDocumentFromTree(node){
                if (tree.getSelectionModel().isSelected(node)) {
                    dataStore.load({
                        params: {
                            node: node.id,
                            start: 0,
                            category: true
                        }
                    });
                }
            };
            
                    },
        initSearch: function(){
            // --------------------------------------------
            // -- SEARCH
            // --------------------------------------------
            var DocumentRecordDefinition = Ext.data.Record.create([{
                name: 'id',
                mapping: 'id'
            }, {
                name: 'name',
                mapping: 'name'
            }, {
                name: 'icon16',
                mapping: 'icon16'
            }, {
                name: 'url',
                mapping: 'url'
            }, {
                name: 'author',
                mapping: 'author'
            }, {
                name: 'creator',
                mapping: 'creator'
            }, {
                name: 'description',
                mapping: 'description'
            }, {
                name: 'title',
                mapping: 'title'
            }, {
                name: 'size',
                mapping: 'size',
                type: 'float'
            }, {
                name: 'modified',
                mapping: 'modified',
                type: 'date'
            }, {
                name: 'created',
                mapping: 'created',
                type: 'date'
            }, {
                name: 'tags',
                mapping: 'tags'
            }]);
            
            // create reader that reads into Topic records
            var reader = new Ext.data.JsonReader({
                totalProperty: 'total',
                root: 'rows',
                id: 'id'
            }, DocumentRecordDefinition);
            var ds = new Ext.data.Store({
                // load using script tags for cross domain
                proxy: new Ext.data.HttpProxy({
                    // url: ALFRESCO_URL+'service/opsoro/documents.json',
                    url: ALFRESCO_URL + 'service/opsoro/search.json',
                    method: 'GET'
                }),
                
                // let it know about the reader
                reader: reader
            });
            
            // Custom rendering Template
            var resultTpl = new Ext.XTemplate('<tpl for="."><div class="search-item">', '<h3 nowrap="nowrap"><img src="{icon16}" class="icon16"/>&nbsp;{name} </h3><span>{created:date("j. M Y")} by {creator}</span></div></tpl>');
            
            var search = new Ext.form.ComboBox({
                store: ds,
                mode: 'remote',
                displayField: 'title',
                typeAhead: false,
                loadingText: 'Searching...',
                width: 570,
                pageSize: 10,
                hideTrigger: true,
                tpl: resultTpl,
                applyTo: 'search',
                itemSelector: 'div.search-item',
                onSelect: function(record){
                    // Show document in new tab and preview panel
                    viewDocument(record.data.url, record.data.name);
                    contentStore.load({
                        params: {
                            node: record.data.id
                        }
                    });
                }
            });
        },
        initDocumentGrid: function(){
            // --------------------------------------------
            // -- DOCUMENT LIST
            // --------------------------------------------
            
            // mapping json data to javascript dataStore for grid display
            var DocumentRecordDefinition = Ext.data.Record.create([{
                name: 'id',
                mapping: 'id'
            }, {
                name: 'name',
                mapping: 'name'
            }, {
                name: 'icon16',
                mapping: 'icon16'
            }, {
                name: 'url',
                mapping: 'url'
            }, {
                name: 'author',
                mapping: 'author'
            }, {
                name: 'creator',
                mapping: 'creator'
            }, {
                name: 'description',
                mapping: 'description'
            }, {
                name: 'title',
                mapping: 'title'
            }, {
                name: 'size',
                mapping: 'size',
                type: 'float'
            }, {
                name: 'modified',
                mapping: 'modified',
                type: 'date'
            }, {
                name: 'is Locked',
                mapping: 'isLocked',
                type: 'boolean'
            }, {
                name: 'created',
                mapping: 'created',
                type: 'date'
            }, {
                name: 'tags',
                mapping: 'tags'
            }]);
            
            // create reader that reads into Topic records
            var reader = new Ext.data.JsonReader({
                totalProperty: 'total',
                root: 'rows',
                id: 'id'
            }, DocumentRecordDefinition);
            
            // create the Data Store
            dataStore = new Ext.data.Store({
                // load using script tags for cross domain
                proxy: new Ext.data.HttpProxy({
                    // callbackParam : 'cb',
                    url: ALFRESCO_URL + 'service/opsoro/documents.json',
                    // url : 'demoData/documents.json',
                    method: 'GET'
                }),
                
                // let it know about the reader
                reader: reader
            });
            
            // pluggable renders
            function renderName(value, p, record){
                // return String.format('<img src="'+ALFRESCO_URL+'{1}"
                // class="icon16"/>&nbsp;{0}', value, record.data['icon16']);
                
                return String.format('<img src="{1}" class="icon16"/>&nbsp;{0}', value, record.data['icon16']);
            }
            
            // the column model has information about grid columns
            // dataIndex maps the column to the specific data field in the data
            // store
            var sm = new Ext.grid.RowSelectionModel();
            var columnModel = new Ext.grid.ColumnModel([{
                header: "Name",
                dataIndex: 'name',
                sortable: true,
                width: 200,
                renderer: renderName
            }, {
                header: "Size",
                dataIndex: 'size',
                sortable: true,
                width: 100
            }, {
                header: "Creator",
                dataIndex: 'creator',
                sortable: true,
                width: 100
            }, {
                header: "Date created",
                dataIndex: 'created',
                width: 110,
                hidden: true,
                sortable: true,
                renderer: Ext.util.Format.dateRenderer('d.m.Y G:i:s')
            }, {
                header: "Date modified",
                dataIndex: 'modified',
                width: 110,
                sortable: true,
                renderer: Ext.util.Format.dateRenderer('d.m.Y G:i:s')
            }, {
                header: "Description",
                dataIndex: 'description',
                width: 200,
                sortable: true,
                hidden: true
            }, {
                header: "Tags",
                dataIndex: 'tags',
                width: 100,
                sortable: false
            }, {
                header: "Title",
                dataIndex: 'title',
                width: 100,
                sortable: true,
                hidden: true
            }, {
                header: "Author",
                dataIndex: 'author',
                width: 100,
                sortable: true,
                hidden: true
            }]);
            var bar = new Ext.PagingToolbar({
                pageSize: 10,
                store: dataStore,
                displayInfo: true,
                height: 27,
                autoWidth: true,
                
                displayMsg: 'Displaying documents {0} - {1} of {2}',
                emptyMsg: "No documents to display"
            });
            // create the document grid
            var grid = new Ext.grid.GridPanel({
                el: 'document-grid',
                id: 'document-grid',
                height: mainTabPanel.getSize().height - 27,
                viewConfig: {
                    forceFit: true
                },
                ds: dataStore,
                cm: columnModel,
                sm: sm,
                // inline toolbars
                tbar: [{
                    text: 'tag it!',
                    tooltip: 'add new tags',
                    iconCls: 'add',
                    handler: tagIt
                }],
                bbar: bar,
                ddGroup: 'GridDD',
                enableDragDrop: true
            });
            grid.render();
            //nasty extJs Bug... first resize doesn't work correctly. Therefore make 1 call here.
            syncGridLayout();
            
            mainTabPanel.on('resize', syncGridLayout);
            
            function syncGridLayout(){
                grid.setHeight(mainTabPanel.getSize().height - 27);
                grid.setWidth(mainTabPanel.getSize().width);
            }
            
            function tagIt(button, event){
                var selections = grid.getSelections();
                var myTagRecord = Ext.data.Record.create([{
                    name: 'name'
                }, {
                    name: 'count'
                }]);
                // create reader that reads into cropContent record
                var myTagReader = new Ext.data.JsonReader({
                    root: "tags",
                    id: "name"
                }, myTagRecord);
                
                // create the Data Store
                var myTagStore = new Ext.data.Store({
                    proxy: new Ext.data.HttpProxy({
                        url: ALFRESCO_URL + 'service/opsoro/tagQuery.json',
                        method: 'GET'
                    }),
                    
                    // let it know about the reader
                    reader: myTagReader,
                    pruneModifiedRecords: true
                });
                // create the Data Store
                var allTagStore = new Ext.data.Store({
                    proxy: new Ext.data.HttpProxy({
                        url: ALFRESCO_URL + 'service/opsoro/tagQuery.json',
                        method: 'GET'
                    }),
                    
                    // let it know about the reader
                    reader: myTagReader,
                    pruneModifiedRecords: true
                });
                
                var myTagView = new Ext.DataView({
                    store: myTagStore,
                    tpl: new Ext.XTemplate('<div id ="tagCloud"><tpl for=".">', '<span class ="select"><span class="tagSelected">{name}</span></span> ', '</tpl></div>'),
                    loadingText: 'Loading tags...',
                    autoHeight: false,
                    selectedClass: 'tagSelected',
                    multiSelect: true,
                    simpleSelect: true,
                    itemSelector: 'span.select'
                });
                myTagView.on("click", function(vw, index, node, e){
                    if (node.tagName == 'SPAN') {
                        Ext.Ajax.request({
                            url: ALFRESCO_URL +
                            'service/opsoro/tagActions',
                            method: 'POST',
                            success: function(){
                                myTagStore.reload();
                                trigger.focus();
                            },
                            params: {
                                t: myTagView.getSelectedRecords()[0].data.name,
                                n: grid.getSelections()[0].data.id.replace('workspace://SpacesStore/', ''),
                                a: 'remove'
                            }
                        });
                    }
                });
                function loadAllTags(store, records, index){
                    var tagNames = '';
                    var count = myTagStore.getCount();
                    for (var i = 0; i < count; i++) {
                        var record = myTagStore.getAt(i);
                        tagNames = tagNames + record.id + '//';
                    }
                    
                    if (tagNames != '') {
                        allTagStore.load({
                            params: {
                                allTags: tagNames
                            }
                        });
                    }
                    else {
                        allTagStore.load();
                    }
                }
                myTagStore.on("load", loadAllTags);
                
                var allTagView = new Ext.DataView({
                    store: allTagStore,
                    tpl: new Ext.XTemplate('<div id ="tagCloud">All tags: <tpl for=".">', '<span class ="select"><span class="tagName{count}">{name}</span><span class="tagCount">({count})</span></span> ', '</tpl></div>'),
                    loadingText: 'Loading tags...',
                    autoHeight: false,
                    selectedClass: 'tagSelected',
                    multiSelect: true,
                    simpleSelect: true,
                    itemSelector: 'span.select'
                });
                
                allTagView.on("click", function(vw, index, node, e){
                    if (node.tagName == 'SPAN') {
                        addTag(allTagView.getSelectedRecords()[0].data.name);
                    }
                });
                
                function addTag(tag){
                    if (tag != null && tag != '') {
                        Ext.Ajax.request({
                            url: ALFRESCO_URL + 'service/opsoro/tagActions',
                            method: 'POST',
                            success: function(){
                                myTagStore.reload();
                                
                            },
                            params: {
                                t: tag,
                                n: grid.getSelections()[0].data.id.replace('workspace://SpacesStore/', ''),
                                a: 'add'
                            }
                        });
                        trigger.reset();
                        trigger.focus();
                    }
                }
                
                var trigger = new Ext.form.TriggerField({
                    name: 'tag',
                    id: 'tagTrigger',
                    hideLabel: true,
                    allowBlank: true,
                    triggerClass: 'x-form-tag-trigger',
                    onTriggerClick: function(){
                        addTag(this.getValue());
                    }
                });
                function closeTagWindow(){
                    tagWindow.hide();
                    tagStore.reload();
                    dataStore.reload();
                }
                
                var taggingForm = new Ext.form.FormPanel({
                    labelWidth: 75,
                    url: ALFRESCO_URL + 'service/opsoro/tagActions',
                    frame: true,
                    id: 'taggingForm',
                    // title : 'Simple Form',
                    bodyStyle: 'padding:5px 5px 5px 5px',
                    
                    width: 350,
                    defaults: {
                        width: 230
                    },
                    defaultType: 'trigger',
                    
                    items: [trigger, {
                        xtype: 'fieldset',
                        title: 'Tagged',
                        collapsible: false,
                        autoHeight: true,
                        autoWidth: true,
                        items: [myTagView]
                    }, {
                        xtype: 'fieldset',
                        title: 'All Tags',
                        collapsible: true,
                        autoHeight: true,
                        autoWidth: true,
                        items: [allTagView]
                    }]
                });
                
                // tagging momentan erstmal nur singleselect...:-(
                var tagWindow = new Ext.Window({
                    items: [taggingForm],
                    title: 'Tagging ' + selections[0].data.name,
                    layout: 'fit',
                    width: 500,
                    height: 300,
                    closeAction: 'close',
                    plain: true,
                    modal: true,
                    focus: function(){
                        trigger.focus();
                    },
                    keys: [{
                        key: [10, 13],
                        fn: function(){
                            addTag(trigger.getValue());
                        }
                    }, {
                        key: 27,
                        fn: function(field, e){
                            e.preventDefault();
                            closeTagWindow();
                        }
                    }],
                    buttons: [{
                        text: 'Close',
                        handler: closeTagWindow
                    }]
                });
                myTagStore.load({
                    params: {
                        n: selections[0].data.id
                    }
                });
                
                tagWindow.show(this);
                
            }
            
            grid.on('rowclick', updateProps)
            function updateProps(grid, dataIndex){
                var dataRow = dataStore.getAt(dataIndex);
                propsGrid.setSource(dataRow.data);
                
            }
            /*grid.on('bodyresize', resized)
             function resized(panel,width, height ) {
             
             mainTabPanel.getSize().height - 27
             }*/
            grid.on('rowclick', previewDocument)
            function previewDocument(grid, dataIndex){
                var dataRow = dataStore.getAt(dataIndex);
                var nodeId = dataRow.data.id;
                contentStore.load({
                    params: {
                        node: nodeId
                    }
                
                });
            }
            grid.on('rowdblclick', viewDocumentOfGrid);
            function viewDocumentOfGrid(grid, dataIndex){
                var dataRow = dataStore.getAt(dataIndex);
                viewDocument(dataRow.data.url, dataRow.data.name);
            }
        },
        init: function(){
            this.initLayout();
            this.initFolderTree();
            this.initCategoryTree();
            this.initDocumentGrid();
            this.initSearch();
            this.initTagCloud();
        }
    };
}
();
Ext.EventManager.onDocumentReady(Opsoro.init, Opsoro, true);
