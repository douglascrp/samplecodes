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
MyAlfrescoGrid = function(limitColumns, webscriptUrl, webscriptParam){


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
    }]);
    
    // create reader that reads into document records
    var reader = new Ext.data.JsonReader({
        root: 'rows',
        id: 'id'
    }, DocumentRecordDefinition);
    
    // create the Data Store
    var myFilesStore = new Ext.data.Store({
        // load using ScriptTagProxy and callback if you're using cross domain requests
        proxy: new Ext.data.HttpProxy({
            // callbackParam : 'cb',
            url: webscriptUrl,
            method: 'GET'
        }),
        
        // let it know about the reader
        reader: reader
    });
    if (webscriptParam) {
        myFilesStore.load({
            params: webscriptParam
        });
    }
    else {
        myFilesStore.load();
    }
    
    
    var columns = [{
        id: 'document',
        header: "Document",
        width: 160,
        sortable: true,
        dataIndex: 'name',
        renderer: renderName
    }, {
        header: "Author",
        width: 75,
        sortable: true,
        dataIndex: 'author'
    }, {
        header: "Creator",
        width: 75,
        sortable: true,
        dataIndex: 'creator'
    }, {
        header: "Created",
        width: 85,
        sortable: true,
        renderer: Ext.util.Format.dateRenderer('m/d/Y'),
        dataIndex: 'created'
    }, {
        header: "is Locked",
        width: 85,
        sortable: true,
        dataIndex: 'is Locked'
    }];
    
    // allow samples to limit columns
    if (limitColumns) {
        var cs = [];
        for (var i = 0, len = limitColumns.length; i < len; i++) {
            cs.push(columns[limitColumns[i]]);
        }
        columns = cs;
    }
    // pluggable renderer: render with icon16
    function renderName(value, p, record){
        return String.format('<img src="' + '{1}" class="icon16"/>&nbsp;{0}', value, record.data['icon16']);
    }
    
    
    MyAlfrescoGrid.superclass.constructor.call(this, {
        store: myFilesStore,
        columns: columns,
        autoExpandColumn: 'document',
        height: 250,
        width: 600
    });
    
    this.on('rowdblclick', viewDocumentOfGrid);
    function viewDocumentOfGrid(grid, dataIndex){
        var dataRow = myFilesStore.getAt(dataIndex);
        viewDocument(dataRow.data.url, dataRow.data.name);
    }
    this.on('rowclick', updateProps)
    function updateProps(grid, dataIndex){
        var dataRow = myFilesStore.getAt(dataIndex);
        propsGrid.setSource(dataRow.data);
        
    }
    this.on('rowclick', previewDocument)
    function previewDocument(grid, dataIndex){
        var dataRow = myFilesStore.getAt(dataIndex);
        var nodeId = dataRow.data.id;
        contentStore.load({
            params: {
                node: nodeId
            }
        
        });
    }
}

Ext.extend(MyAlfrescoGrid, Ext.grid.GridPanel);
