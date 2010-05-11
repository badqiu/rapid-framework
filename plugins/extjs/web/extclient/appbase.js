Ext.override(Ext.Toolbar.TextItem, {
    setText : function(text){
        var element = Ext.get(this.el);
        element.update(text);
    }

});

Ext.override(Ext.form.BasicForm,{   
    findField : function(id){           
        var field = this.items.get(id);           
        if(!field){   
            this.items.each(function(f){   
                if(f.isXType('radiogroup')||f.isXType('checkboxgroup')){   
                    f.items.each(function(c){   
                        if(c.isFormField && (c.dataIndex == id || c.id == id || c.getName() == id)){   
                            field = c;   
                            return false;   
                        }   
                    });   
                }   
                                   
                if(f.isFormField && (f.dataIndex == id || f.id == id || f.getName() == id)){   
                    field = f;   
                    return false;   
                }   
            });   
        }   
        return field || null;   
    }    
});  

Ext.override(Ext.form.ComboBox,{
    initList : function(){

        if(!this.list){
            var cls = 'x-combo-list';

            this.list = new Ext.Layer({
                shadow: this.shadow, cls: [cls, this.listClass].join(' '), constrain:false
            });

            var lw = this.listWidth || Math.max(this.wrap.getWidth(), this.minListWidth);
            this.list.setWidth(lw);
            this.list.swallowEvent('mousewheel');
            this.assetHeight = 0;

            if(this.title){
                this.header = this.list.createChild({cls:cls+'-hd', html: this.title});
                this.assetHeight += this.header.getHeight();
            }

            this.innerList = this.list.createChild({cls:cls+'-inner'});
            this.innerList.on('mouseover', this.onViewOver, this);
            this.innerList.on('mousemove', this.onViewMove, this);
            this.innerList.setWidth(lw - this.list.getFrameWidth('lr'));

            if(this.pageSize){
                this.footer = this.list.createChild({cls:cls+'-ft'});
                this.pageTb = new Ext.PagingToolbar({
                    store:this.store,
                    pageSize: this.pageSize,
                    renderTo:this.footer
                });
                this.assetHeight += this.footer.getHeight();
            }
            
           if(this.bbar){
                this.footer = this.list.createChild({cls:cls+'-ft'});
                this.pageTb =  new Ext.Toolbar({
				renderTo:this.footer,
				items:[this.bbar]
				});
                this.pageTb.render(this.footer);
                this.assetHeight += this.footer.getHeight();
            }
            
            
            if(!this.tpl){
                /**
                * @cfg {String/Ext.XTemplate} tpl The template string, or {@link Ext.XTemplate}
                * instance to use to display each item in the dropdown list. Use
                * this to create custom UI layouts for items in the list.
                * <p>
                * If you wish to preserve the default visual look of list items, add the CSS
                * class name <pre>x-combo-list-item</pre> to the template's container element.
                * <p>
                * <b>The template must contain one or more substitution parameters using field
                * names from the Combo's</b> {@link #store Store}. An example of a custom template
                * would be adding an <pre>ext:qtip</pre> attribute which might display other fields
                * from the Store.
                * <p>
                * The dropdown list is displayed in a DataView. See {@link Ext.DataView} for details.
                */
                this.tpl = '<tpl for="."><div class="'+cls+'-item">{' + this.displayField + '}</div></tpl>';
                /**
                 * @cfg {String} itemSelector
                 * <b>This setting is required if a custom XTemplate has been specified in {@link #tpl}
                 * which assigns a class other than <pre>'x-combo-list-item'</pre> to dropdown list items</b>.
                 * A simple CSS selector (e.g. div.some-class or span:first-child) that will be
                 * used to determine what nodes the DataView which handles the dropdown display will
                 * be working with.
                 */
            }

            /**
            * The {@link Ext.DataView DataView} used to display the ComboBox's options.
            * @type Ext.DataView
            */
            this.view = new Ext.DataView({
                applyTo: this.innerList,
                tpl: this.tpl,
                singleSelect: true,
                selectedClass: this.selectedClass,
                itemSelector: this.itemSelector || '.' + cls + '-item'
            });

            this.view.on('click', this.onViewClick, this);

            this.bindStore(this.store, true);

            if(this.resizable){
                this.resizer = new Ext.Resizable(this.list,  {
                   pinned:true, handles:'se'
                });
                this.resizer.on('resize', function(r, w, h){
                    this.maxHeight = h-this.handleHeight-this.list.getFrameWidth('tb')-this.assetHeight;
                    this.listWidth = w;
                    this.innerList.setWidth(w - this.list.getFrameWidth('lr'));
                    this.restrictHeight();
                }, this);
                this[this.pageSize?'footer':'innerList'].setStyle('margin-bottom', this.handleHeight+'px');
            }
        }
    }
});

