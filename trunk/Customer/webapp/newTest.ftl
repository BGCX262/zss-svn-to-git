<@resource.javascript>

 
    function showOptions() {
        $j('#jive-people-search-options').toggle();
    }

    var selectedUsers = [];

    <#if multiple>

    function pick(userID, userName, avatar, displayName, target) {
        alert('topmessage');
        var idx = -1;
        for (var i = 0; i < selectedUsers.length; i++) {
            if (selectedUsers[i].userID == userID) {
                idx = i;
                break;
            }
        }

        if (idx < 0) {
            // userID does not exist, so add it
            selectedUsers.push({userID: userID, userName: userName, avatar: avatar, displayName: displayName});
        } else {
            // userID exists, remove it
            selectedUsers.splice(idx, 1);
        }

        loadSelected();
    }

    <#else>

    function pick(userID, userName, avatar, displayName, target) {
        target.trigger('select', {
            userID: userID,
            userName: userName,
            avatar: avatar,
            displayName: displayName
        });
        return true;
    }

    </#if>

    function loadSelected() {
   
        // loop through all elements, trim and select
        var displayNames = [];
        selectedUsers.forEach(function(selectedUser) {
            if ($j('#userChk-' + selectedUser.userID).size() > 0) {
                $j('#userChk-' + selectedUser.userID).attr('checked', 'checked');
                $j('#userChk-' + selectedUser.userID).parent().parent().addClass('selected');
            }
            displayNames.push(selectedUser.displayName);
        });
        // update selections text
        $j('#preview').text(displayNames.join(", "));
    }

    function buildSelectedUsersString() {
    
        var selectedUsersString = '';
        selectedUsers.forEach(function(selectedUser) {
            if (selectedUsersString != '') {
                selectedUsersString = selectedUsersString + ",";
            }
            selectedUsersString = selectedUsersString + selectedUser.userID;
        });
        return selectedUsersString;
    }

    function loadPage(href, target) {
        $j.get(href + '&selectedUsers=' + buildSelectedUsersString(), function(html) {
            target.html(html);
            target.find('input:first').focus();
        });
    }

    $j(function() {
        <#assign popupGUID = StringUtils.randomString(6) />
        var popup = $j('.jive-body-popup-container-userpicker').filter(function() {
            return $j(this).hasClass('${popupGUID?js_string}');
        });
        var popupParent = popup.parent();

        <#if multiple>
        <#list selectedUserObjects as selectedUser>
            <#assign avatar><@jive.userAvatar user=selectedUser size=22 useLinks=false showHover=false/></#assign>
            <#assign displayName><@jive.displayUserDisplayName user=selectedUser /></#assign>
            selectedUsers.push({
                userID: '${selectedUser.ID?c}',
                userName: '${selectedUser.username?js_string}',
                avatar: '${avatar?js_string}',
                displayName: '${displayName?js_string}'
            });
        </#list>

        loadSelected();

        $j('#userpicker-addUsersButton').click(function() {
            popupParent.trigger('select', selectedUsers);
            return true;
        });

        </#if>

        <#if (actionErrors.size() > 0)>
        popup.find('#jive-error-box').show();
        </#if>

//vasu first
        // Setup event handling for check boxes
        $j('.jive-table-userpicker td.jive-table-cell-checkbox input[type=checkbox]').click(function(e) {
            var userID = this.id.replace('userChk-', '');
            var userName = $j(this).siblings('[name*=userName]').val();
            var avatar = $j('#' + this.id + '-avatar').html();
            var displayName = $j('#' + this.id + '-displayName').text();
            $j('#' + this.id).parent().parent().toggleClass('selected');
            pick(userID, userName, avatar, displayName, popupParent);
            return true;
        });

        $j('a.userpicker-page-link').click(function(e) {
            var baseHref = this.href
            loadPage(baseHref, popupParent);
            return false;
        });

        <#if view?exists && view == 'search'>
        $j('#userpicker-options-link').click(function(e) {
            showOptions();
            return false;
        });

        <#if query?has_content>
        $j('.userpicker-search-pagination').click(function(e) {
            var page = this.id.replace('search-paginate-', '');
            $j('#profilesearchform-start').val(page);
            $j('#profilesearchform').submit();
            return false;
        });

        $j('.userpicker-sort-link').click(function(e) {
            var sort = this.id.replace('userpicker-sort-link-', '');
            $j('#profilesearchform-sort').val(sort);
            $j('#profilesearchform').submit();
            return false;
        });
        </#if>

        </#if>

        <#assign globalSearching><@s.text name="global.searching" /></#assign>
        $j('#profilesearchform').ajaxForm({
            beforeSubmit: function(formData, form, options) {
                formData.push({
                    name: 'selectedUsers',
                    value: buildSelectedUsersString()
                });
                $j('#people-search-submit').attr('disabled', 'disabled');
                $j('#people-search-submit').val('${globalSearching?js_string}...');
            },
            success: function(html) {
                popupParent.html(html);
            }
        });

    });

    // people.ftl
    var timeout = ${facetTimeout?c!30000};

    // people.js
    var acVals = {};
    var acFuncs = {};
    var filterIndexMap = {};
    var filterTypeMap = {};
    var appliedFilters = [];
    var searchFieldFocused = false;

    function doSearch(btn) {
        //if someone has focused on the search field, and clicks the search button, set sort to relevance (value = '')
        $j('#sort-field option').val('');
        $j(btn).attr('disabled', 'disabled');
        $j(btn).val(searchingText);
        $j(btn).closest('form').submit();
        $j(window).unload( function () { $j(btn).removeAttr('disabled'); } );
    }

    function showOptions() {
        $j('#jive-people-search-options').toggle();
    }

    function getProfileFieldValues(q, nonSelectedLabel) {
        ProfileFieldValueStats.caclulateFacets(q, {
            callback:function(stats) {
                
                for (var fieldID in stats.resultMap) {
                    if ($j.inArray(fieldID, appliedFilters) == -1) {
                        var fType = filterTypeMap['f' + fieldID];
                        var fIndex = filterIndexMap['f' + fieldID];
                        var valueCountMap = stats.resultMap[fieldID];
                        var fName = 'filter-field-' + fieldID;
                        var field = $j('[id="'+ fName +'"]');
                        var val, safeVal;
                        var hasValues = false;

                        //deal with generic select fields
                        if (field.find('option').length > 0) {
                            //key is field ID
                            field.html('');
                            field.append($j('<option/>', { value: "", text: nonSelectedLabel }));
                            for (val in valueCountMap) {
                                if (val) {
                                    hasValues = true;
                                    field.append($j('<option/>', {
                                        value: val,
                                        text: val + " (" + valueCountMap[val] + ")"
                                    }));
                                }
                            }
                        }
                        else if (acFuncs[fIndex]) {
                            acVals[fIndex] = [];
                            for (val in valueCountMap) {
                                var safeFuncVal = jive.util.escapeHTML(val.split('|').first());
                                if (safeFuncVal) {
                                    hasValues = true;
                                    acVals[fIndex].push(safeFuncVal);
                                }
                            }
                            if (acVals[fIndex].length > 0) {
                                acFuncs[fIndex]();
                            }
                        }

                        // dont display field if empty
                        if (!hasValues) {
                            $j('#filter-table-row-' + fieldID).hide();
                        }
                    }
                }

                convertSelectsIntoLinks();
            },
            errorHandler:function(message) {
                if (console){
                    console.log(message);
                }
                $j("#filter-sidebar-body").hide();
            },
            timeout: timeout
        });
    }

    function convertSelectsIntoLinks() {
        $j('#select.filterOption').each(function() {
            var select = $j(this),
                opts = select.find('option');
            if (opts.length > 1) {      //there will always be the "select one" option
                if (opts.length - 1 <= linkThreshold) {
                    select.replaceWith(
                        $j('<span/>').append(
                            $j('<input/>', { type: 'hidden', id: select.attr('id'), name: select.attr('name') })
                        ).append(
                            opts.filter(function() {
                                return !!this.value;  // Filter out options with empty values.
                            }).map(function() {
                                // Translate each option into a link with the same text.
                                return $j('<a/>', { href: '#', text: $j(this).text() })
                                    .click(submitLink.partial(select.id, this.value));
                            }).toArray().reduce(function(links, link) {
                                // Combine links into a single structure separated by commas.
                                return links.after(document.createTextNode(', ')).after(link);
                            })
                        )
                    );
                }
            }
        });
    }

    function submitLink(selID, value) {
        $j('[id="'+ selID +'"]').val(value);
        submitForm();
    }

    function submitPrefix(letter) {
        $j('#profilesearchform [name=prefix]').val(letter);
        $j('#profilesearchform [name=view]').val('alphabetical');
        submitForm()
    }

    function submitTag(tag) {
        $j('#profilesearchform [name=tag]').val(tag);
        submitForm();
    }

    function clearFilter(fieldID) {
        $j('[id="filter-field-'+ fieldID +'"]').val('');
        $j('[id="filter-field-'+ fieldID +'.minValue"]').val('');
        $j('[id="filter-field-'+ fieldID +'.maxValue"]').val('');
        submitForm()
    }

    function clearPrefix() {
        $j('#filter-prefix').val('');
        submitForm()
    }

    function clearQuery() {
        $j('#query').val('');
        submitForm()
    }

    function clearTag() {
        $j('#tag').val('');
        submitForm();
    }

    function clearView() {
        $j('#profilesearchform-view').val('');
        submitForm();
    }

    function clearOnline() {
        $j('#online-filter').val('');
        if ($j('#profilesearchform-view').val() == 'online') {
            clearView();
        } else {
            submitForm();
        }
    }

    function clearCommunity() {
        $j('community').val('');
        submitForm();
    }

    function clearRecentlyAdded() {
        $j('#recently-added-filter').val('');
        if ($j('#profilesearchform-view').val() == 'newest') {
            clearView();
        } else {
            submitForm();
        }
    }

    function submitForm() {
        $j('#profilesearchform').submit();
    }

    function populateActivity(q) {
        var params = $j.param({ queryEncoded: q });
        $j('#recentActivity').load(resultActivityURL, params, function() {
            $j('#recentActivity').show();
        });
    }
    
    //Vasu last
    
    function selectAllCheckBox(status)
		{
		if(status)
		{
  		  	 $j("INPUT[type='checkbox']").attr('checked', true);	
 	 		 var matches = [];
 	 		  alert('selected');
			$j(".userCheckbox").each(function() {
			 
			   // alert(this +' value' +this.value + ' id ' + this.id);
				 matches.push(this);
				 
		});
		alert(matches.length);
	          
	     for ( var i=0; i<matches.length; i++ ){
	     
 	     	 var userID = matches[i].id.replace('userChk-', '');
             var userName = $j(matches[i]).siblings('[name*=userName]').val();
             var avatar = $j('#' + matches[i].id + '-avatar').html();
             var displayName = $j('#' + matches[i].id + '-displayName').text();
            	$j('#' + this.id).parent().parent().toggleClass('selected');        
             pick(userID, userName, avatar, displayName);
           
           // return true;

		}
  		 
  		 }
 		 else
 		 {
 		  		
 	 		 var matches = [];
 	 		  alert('un selected');
			$j(".userCheckbox").each(function() {
		 	   	 matches.push(this);
		     });
		 alert(matches.length);
	          
	     for ( var i=0; i<matches.length; i++ ){
	       $j("INPUT[type='checkbox']").attr('checked', false);
 		
	         alert('id is   -   '+matches[i].id);
	     	 var userID = matches[i].id.replace('userChk-', '');
	    	   alert('modified User ID '+userID);
            var userName = $j(matches[i]).siblings('[name*=userName]').val();
            alert('Modified User Name is '+userName);
            var avatar = $j('#' + matches[i].id + '-avatar').html();
            alert('Modified Avathar '+avatar);
            var displayName = $j('#' + matches[i].id + '-displayName').text();
           	 alert('Modified display Name is '+ displayName);
           	 alert('test alertthis'); 
           	$j('#' + this.id).parent().parent().toggleClass('selected');        
            //alert('Final popupParent' + popupParent.value);
            pick(userID, userName, avatar, displayName);
           
           // return true;

		}
 		  
 		  
 		 }
		}
		
		 
	 

</@resource.javascript>

<#assign hasResultKey = queryEncoded?has_content/>
<#assign filterIndex = 0>
<#assign ffm = filterableFieldMap />

<#include "/template/global/include/profile-macros.ftl"  />
<#if !hasResultKey>
<@resource.dwr file="ProfileFieldValueStats" />
</#if>

<div id="jive-body-popup-container" class="jive-body-popup-container-userpicker ${popupGUID?html}">

    <#if (actionErrors.size() > 0)>
        <div id="jive-error-box" class="jive-error-box" style="visibility: hidden;">
            <span class="jive-icon-med jive-icon-redalert"></span>
            <#list actionErrors as actionError>
                ${actionError}
            </#list>
        </div>
    </#if>

    <#if actionMessages?size gt 0>
    <#list actionMessages as actionMessage>
        <div class="jive-info-box">
            <div>
                <span class="jive-icon-med jive-icon-info"></span>
            ${actionMessage?html}
            </div>
        </div>
    </#list>
    </#if>

    <form action="<@s.url action="user-autocomplete-modal" />" method="get" id="profilesearchform" name="profilesearchform">

    <#assign statusLevelEnabled = jiveContext.getStatusLevelManager().isStatusLevelsEnabled()>

    <div class="jive-body-tabbar">
        <span id="jive-aToZ-tab" class="jive-body-tab<#if view == 'alphabetical'> jive-body-tabcurrent</#if>">
            <a class="userpicker-page-link" title="<@s.text name="user.picker.vwByUsrname.tooltip" />" href="<@s.url value="${actionMappingName}.jspa?view=alphabetical&showAddressbook=${showAddressbook?string}" />&multiple=${multiple?string}"><@s.text name="user.picker.tab.browse.label" /></a>
        </span>
        <#if friendingEnabled>
        <span id="jive-aToZ-tab" class="jive-body-tab<#if view == 'connections'> jive-body-tabcurrent</#if>">
            <#assign friendingLabel><#if friendingReflexive><@s.text name="profile.tab.friends" /><#else><@s.text name="profile.tab.connections" /></#if></#assign>
            <a class="userpicker-page-link" title="${friendingLabel}" href="<@s.url value="${actionMappingName}.jspa?view=connections&showAddressbook=${showAddressbook?string}" />&multiple=${multiple?string}">${friendingLabel}</a>
        </span>
        </#if>
        <#if (orgChartingEnabled)>
        <span id="jive-aToZ-tab" class="jive-body-tab<#if view == 'orgchart'> jive-body-tabcurrent</#if>">
            <a class="userpicker-page-link" title="<@s.text name="profile.gr.org.title" />" href="<@s.url value="${actionMappingName}.jspa?view=orgchart&showAddressbook=${showAddressbook?string}" />&multiple=${multiple?string}"><@s.text name="profile.gr.org.title" /></a>
        </span>
        </#if>
        <span id="jive-search-tab" class="jive-body-tab<#if view == 'search'> jive-body-tabcurrent</#if>">
            <a class="userpicker-page-link" title="<@s.text name="user.picker.vwBySearch.tooltip" />" href="<@s.url value="${actionMappingName}.jspa?view=search&showAddressbook=${showAddressbook?string}" />&multiple=${multiple?string}"><@s.text name="user.picker.tab.search.label" /></a>
        </span>
        <span id="jive-online-tab" class="jive-body-tab<#if view == 'online'> jive-body-tabcurrent</#if>" style="display: none;">
            <a class="userpicker-page-link" title="<@s.text name="user.picker.vwOnlnUsrs.tooltip" />" href="<@s.url value="${actionMappingName}.jspa?view=online&showAddressbook=${showAddressbook?string}" />&multiple=${multiple?string}"><@s.text name="user.picker.tab.whosOnline.label" /></a>
        </span>
        <span id="jive-newest-tab" class="jive-body-tab<#if view == 'newest'> jive-body-tabcurrent</#if>">
            <a class="userpicker-page-link" title="<@s.text name="user.picker.vwByRegDate.tooltip" />" href="<@s.url value="${actionMappingName}.jspa?view=newest&showAddressbook=${showAddressbook?string}" />&multiple=${multiple?string}"><@s.text name="user.picker.tab.newest.label" /></a>
        </span>
        <span id="jive-addressbook-tab" class="jive-body-tab<#if view == 'addressbook'> jive-body-tabcurrent</#if>" <#if !showAddressbook>style="display:none"</#if>>
            <a class="userpicker-page-link" title="<@s.text name="user.picker.addressBk.tooltip" />" href="<@s.url value="${actionMappingName}.jspa?view=addressbook&showAddressbook=true" />&multiple=${multiple?string}"><@s.text name="user.picker.tab.address.label"/></a>
        </span>

    </div>


    <div id="jive-people-results" class="clearfix">

        <div id="jive-people-resultsbar">

            <#if view?? && view == 'alphabetical'>
                <span id="jive-people-resultbar-alphabetical">
                    <#assign seq = ['a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z']>
                    <#if !prefix?has_content>
                        <span class="jive-alpha-link jive-alpha-link-selected"><@s.text name="global.all"/></span> |
                    <#else>
                        <span class="jive-alpha-link"><a class="userpicker-page-link" href="<@s.url value="${actionMappingName}!input.jspa?&multiple=${multiple?string}&view=alphabetical" />"><@s.text name="global.all"/></a></span> |
                    </#if>
                    <#list seq as x>
                        <#if prefix?exists && prefix == x>
                            <span class="jive-alpha-link  jive-alpha-link-selected">${x?upper_case}</span>
                        <#else>
                            <span class="jive-alpha-link"><a class="userpicker-page-link" href="<@s.url value="${actionMappingName}!input.jspa?&multiple=${multiple?string}&view=alphabetical&prefix=${x}" />">${x?upper_case}</a></span>
                        </#if>
                    </#list>
                </span>
            </#if>

            <#if (view?? && view == 'connections' && friendingEnabled && (userRelationshipLists.size() > 0))>
                <span id="jive-people-resultbar-connections">
                    <@s.text name="profile.friends.filterbylabel.title" /><@s.text name="global.colon" />
                    <#if labelID <= 0 >
                        <span class="jive-alpha-link jive-alpha-link-selected"><@s.text name="global.all"/></span> |
                    <#else>
                        <span class="jive-alpha-link"><a class="userpicker-page-link" href="<@s.url value="${actionMappingName}!input.jspa?&multiple=${multiple?string}&view=connections" />"><@s.text name="global.all"/></a></span> |
                    </#if>
                    <#list userRelationshipLists as userRelationshipList>
                        <#if (userRelationshipList.ID == labelID)>
                            <span class="jive-alpha-link  jive-alpha-link-selected">${userRelationshipList.name?html}</span>
                        <#else>
                            <span class="jive-alpha-link"><a class="userpicker-page-link" href="<@s.url value="${actionMappingName}!input.jspa?&multiple=${multiple?string}&view=connections&labelID=${userRelationshipList.ID?c}" />">${userRelationshipList.name?html}</a></span>
                        </#if>
                    </#list>
                </span>
            </#if>

            <#if view?? && view == 'search'>

                <div id="jive-people-search">

                    <#if (hasResultKey && results?size > 1)>
                        <script type="text/javascript">
                            <@s.text name="search.user.filter.nonselected.label" id="nonSelectedLabel"/>
                            $j(document).ready(function() {
                                var q = '${queryEncoded?js_string}';
                                getProfileFieldValues(q, '${nonSelectedLabel?js_string}');
                            });
                        </script>
                    </#if>

                    <@s.textfield theme="simple" id="query" name="query" size="50" class="jive-global-search-field jive-people-search-form-input"/>

                    <input type="hidden" name="resultTypes" value="COMMUNITY"/>
                    <input type="hidden" id="profilesearchform-sort" name="sort" value="${sort!"relevance"}" />
                    <input type="hidden" id="profilesearchform-start" name="start" value="0" />
                    <input type="hidden" name="multiple" value="${multiple?string}" />
                    <input type="hidden" name="view" value="search" />
                    <input id="people-search-submit" type="submit" class="jive-people-search-form-submit" value="<@s.text name='global.search'/>" />

                    <a id="userpicker-options-link" href="#" class="font-color-meta"><@s.text name="user.picker.more_options.link" /></a>

                    <div id="jive-people-search-options" style="display:none">
                        <@s.checkbox theme="simple" name="usernameEnabled" id="usernameEnabled" fieldValue="true"/>
                        <label for="usernameEnabled" class="jive-description"><@s.text name="global.username"/></label>
                        &nbsp;&nbsp;

                        <@s.checkbox theme="simple" name="nameEnabled" id="nameEnabled" fieldValue="true"/>
                        <label for="nameEnabled" class="jive-description"><@s.text name="global.name"/></label>
                        &nbsp;&nbsp;

                        <@s.checkbox theme="simple" name="emailEnabled" id="emailEnabled" fieldValue="true"/>
                        <label for="emailEnabled" class="jive-description"><@s.text name="global.email"/></label>
                        &nbsp;&nbsp;

                        <#assign fields = jiveContext.getProfileFieldManager().getProfileFields() />
                        <#if (fields.size() > 0)>
                            <@s.checkbox theme="simple" name="profileEnabled" id="profileEnabled" fieldValue="true"/>
                            <label for="profileEnabled" class="jive-description"><@s.text name="global.profile"/></label>
                        </#if>

                        <!-- Filterable profile fields -->
                        <#if (hasResultKey)>
                        <div class="jive-people-browse-filters jive-people-browse-memberstats">
                        <#list ffm.keySet() as pfs>
                            <#if (pfs.field.filterable)>
                                <@userFilter field=pfs.field values=ffm.get(pfs) applied=pfs.applied isQuery=hasResultKey/>
                            </#if>
                        </#list>
                        </div>
                        </#if>
                    </div>
                    <#if (appliedFilterMap?has_content)>
                        <div id="appliedFilters" class="jive-people-applied-filters">
                            <ul>
                                <#list appliedFilterMap.keySet() as appliedField>
                                    <#assign filter = appliedFilterMap.get(appliedField)/>
                                    <script type="text/javascript">appliedFilters[appliedFilters.length] = '${appliedField.ID?c}';</script>
                                    <#if (!filter.empty)>
                                        <#assign showClearButton = true/>
                                        <li>
                                            <a href="#" title="<@s.text name="global.remove"/>" onclick="clearFilter('${appliedField.ID?c}');return false;">
                                            <span><strong>${appliedField.name?html}:</strong>

                                            <#if ((filter.minValue?has_content) && (filter.maxValue?has_content))>
                                                <@s.text name="search.user.filter.range.between">
                                                    <@s.param>${filter.minValue?html}</@s.param>
                                                    <@s.param>${filter.maxValue?html}</@s.param>
                                                </@s.text>
                                            <#elseif (filter.minValue?has_content)>
                                                <@s.text name="search.user.filter.range.min">
                                                    <@s.param>${filter.minValue?html}</@s.param>
                                                </@s.text>
                                            <#elseif (filter.maxValue?has_content)>
                                                <@s.text name="search.user.filter.range.max">
                                                    <@s.param>${filter.maxValue?html}</@s.param>
                                                </@s.text>
                                            <#else>
                                                 ${filter.value?html}
                                            </#if>
                                            </span></a>
                                        </li>
                                    </#if>
                                </#list>
                            </ul>
                        </div>
                    </#if>
                </div>

                <#if (view?? && view == 'search' && query?has_content)>
                    <#if (results.size() == 0)>
                        <#-- empty -->
                    <#else>
                        <span id="jive-peoplesearch-resultbar-sort">
                            <!--vasu -->
                          <input type="checkbox" class="checkbox"  name="selectAll" id="selectAll" onBlur="selectAllCheckBox(this.checked);" onSelect="selectAllCheckBox(this.checked);"  onChange="selectAllCheckBox(this.checked);"/>
                           
                            <@s.text name="global.sort_by"/><@s.text name="global.colon"/>
                            <@s.set name="relevance" value="getText('search.user.relevance.link')"/>
                            <@s.set name="statuslevel" value="getText('people.status_level.link')"/>
                            <#if sort == 'relevance' || sort == ''>
                                <span class="jive-alpha-link jive-alpha-link-selected">${relevance}</span>
                            <#else>
                                <span class="jive-alpha-link">
                                    <a id="userpicker-sort-link-relevance" class="userpicker-sort-link" href="#" title="<@s.text name="user.picker.srtByRlvnc.tooltip" />">${relevance}</a>
                                </span>
                            </#if>
                            <#if (jiveContext.getStatusLevelManager().isStatusLevelsEnabled())>
                                <#if sort == 'statusLevel'>
                                    <span class="jive-alpha-link jive-alpha-link-selected">${statuslevel}</span>
                                <#else>
                                    <span class="jive-alpha-link">
                                        <a id="userpicker-sort-link-statusLevel" class="userpicker-sort-link" href="#" title="<@s.text name="user.picker.srtByStsLvl.tooltip" />">${statuslevel}</a>
                                    </span>
                                </#if>
                            </#if>
                            <#if sort == 'username'>
                                <span class="jive-alpha-link jive-alpha-link-selected"><@s.text name="global.username" /></span>
                            <#else>
                                <span class="jive-alpha-link">
                                    <a id="userpicker-sort-link-username" class="userpicker-sort-link" href="#" title="<@s.text name="user.picker.srtByUsrnam.tooltip" />"><@s.text name="global.username" /></a>
                                </span>
                            </#if>
                        </span>
                    </#if>
                </#if>
            </#if>


            <#assign baseURL = "${actionMappingName}.jspa">
            <#if (paginator.numPages > 1)>
                <#if view?exists && view == 'search'>
                    <span class="jive-pagination">
                        <span class="jive-pagination-numbers">
                            <#list paginator.getPages(3) as page>
                            <#if (page?exists)>
                                <a id="search-paginate-${page.start?c}" href="#" class="userpicker-search-pagination"
                                    <#if (page.start == start)>class="jive-pagination-current"</#if> >${page.number}</a>
                            <#else>
                                <span>...</span>
                            </#if>
                            </#list>
                        </span>
                        <span class="jive-pagination-prevnext">
                            <#if (paginator.previousPage)>
                                <a id="search-paginate-${paginator.previousPageStart?c}" class="userpicker-search-pagination" href="#"
                                    class="jive-pagination-prev"><@s.text name="global.previous"/></a>
                            <#else>
                                <span class="jive-pagination-prev-none"><@s.text name="global.previous"/></span>
                            </#if>
                            <#if (paginator.nextPage)>
                                <a id="search-paginate-${paginator.nextPageStart?c}" class="userpicker-search-pagination" href="#"
                                    class="jive-pagination-next"><@s.text name="global.next"/></a>
                            <#else>
                                <span class="jive-pagination-next-none"><@s.text name="global.next"/></span>
                            </#if>
                        </span>
                    </span>

                <#else>
                    <span class="jive-pagination">
                        <span class="jive-pagination-numbers">
                            <#list paginator.getPages(3) as page>
                            <#if (page?exists)>
                                <a href="<@s.url value="${baseURL}" />?start=${page.start?c}&view=${view?html}&multiple=${multiple?string}<#if prefix?exists>&prefix=${prefix?html}</#if>"
                                    class="userpicker-page-link<#if (page.start == start)> jive-pagination-current</#if>" >${page.number}</a>
                            <#else>
                                <span>...</span>
                            </#if>
                            </#list>
                        </span>
                        <span class="jive-pagination-prevnext">
                            <#if (paginator.previousPage)>
                                <a href="<@s.url value="${baseURL}" />?start=${paginator.previousPageStart?c}&view=${view?html}&multiple=${multiple?string}<#if prefix?exists>&prefix=${prefix?html}</#if>"
                                class="userpicker-page-link jive-pagination-prev"><@s.text name="global.previous"/></a>
                            <#else>
                                <span class="jive-pagination-prev-none"><@s.text name="global.previous"/></span>
                            </#if>
                            <#if (paginator.nextPage)>
                                <a href="<@s.url value="${baseURL}" />?start=${paginator.nextPageStart?c}&view=${view?html}&multiple=${multiple?string}<#if prefix?exists>&prefix=${prefix?html}</#if>"
                                class="userpicker-page-link jive-pagination-next"><@s.text name="global.next"/></a>
                            <#else>
                                <span class="jive-pagination-next-none"><@s.text name="global.next"/></span>
                            </#if>
                        </span>
                    </span>
                </#if>
            </#if>
        </div>

        <div <#if multiple>id="jive-user-picker-results-multiple"<#else>id="jive-user-picker-results-single"</#if> class="jive-user-picker-results">
            <div class="jive-table jive-table-userpicker">
            <#if (view?? && ((view == 'connections' && friendingEnabled) || (view == 'orgchart') && orgChartingEnabled))>
                <div id="jive-table-userpicker-body" class="jive-table-userpicker-body-relationships">
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tbody>
                            <#if userRelationshipViews.size() == 0>
                                <p class="jive-box-empty-message font-color-meta">
                                    <#if (view == 'connections')>
                                        <#if friendingReflexive>
                                            <@s.text name="profile.friends.self.nofriends.label" />
                                        <#else>
                                            <@s.text name="profile.friends.self.nofollow.label" />
                                        </#if>
                                    <#elseif (view == 'orgchart')>
                                        <@s.text name="profile.org.orgchart.undefined.text" />
                                    </#if>
                                </p>
                            </#if>
                            <#list userRelationshipViews as userRelationshipView>
                            <@userRow result=userRelationshipView.person idx=userRelationshipView_index />
                            </#list>
                        </tbody>
                    </table>
                </div>
            <#elseif (view?? && view = 'addressbook')>
                 <div id="jive-table-userpicker-body" class="jive-table-userpicker-body-relationships">
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tbody>
                            <#list addressbook as user>
                            <@userRow result=user idx=user_index />
                            </#list>
                        </tbody>
                    </table>
                </div>
            <#elseif (view??)>
                <div id="jive-table-userpicker-body" <#if view?? && view == 'search'>class="jive-table-userpicker-body-search"</#if>>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tbody>
                            <#if results.size() == 0>
                                <p class="jive-box-empty-message font-color-meta">
                                    <#if (view == 'search' && query?has_content)>
                                        <@s.text name="search.noResultsForQuery.text" /> <strong>${query?html}</strong>
                                    <#elseif (view == 'alphabetical')>
                                        <@s.text name="search.noResultsForQuery.text" /> <strong>${prefix!}</strong>
                                    <#elseif (view == 'newest')>
                                        <@s.text name="people.newest.empty.text" />
                                    <#else>
                                        <@s.text name="search.noResultsQuery.text" />
                                    </#if>
                                </p>
                            </#if>
                            <#list results as user>
                            <@userRow result=user idx=user_index />
                            </#list>
                        </tbody>
                    </table>
                </div>
            </#if>
            </div>
        </div>

    </div>


    <#if multiple>
    <div class="jive-user-search-mult-preview">
        <div class="jive-user-search-mult-preview-padding">

            <div class="jive-user-search-mult-preview-users">
                <strong class="font-color-meta"><@s.text name="user.picker.selected_usrs.label" /><@s.text name="global.colon" /></strong>
                <span id="preview"></span>
            </div>

            <div class="jive-user-search-mult-preview-buttons">
                <input type="button" id="userpicker-addUsersButton" value="<@s.text name="user.picker.addSlctdUsrs.button" />"/>
            </div>

        </div>
    </div>
    </#if>
    

    </form>

</div>

<@resource.javascript ajaxOutput="true"/> 
<#macro userRow result idx>
<#if result.enabled>
 <#assign online = jiveContext.getPresenceManager().isOnline(result) />
<#assign statusLevelEnabled = jiveContext.getStatusLevelManager().isStatusLevelsEnabled()>
<#assign viewingSelf = (user.ID?c == result.ID?c)>
  <tr class="jive-table-row-<#if (idx % 2 == 0)>odd<#else>even</#if>" >
        <td class="jive-table-cell-checkbox">
            <input id="userChk-${result.ID?c}" type="checkbox"  class="userCheckbox" />
            <input type="hidden" name="userChk-${result.ID?c}-userName" value="${result.username?html}" />
            <span id="userChk-${result.ID?c}-displayName" style="display: none;">${SkinUtils.getDisplayName(result)}</span>
        </td>
        <td class="jive-table-cell-avatar">
            <label for="userChk-${result.ID?c}"><span id="userChk-${result.ID?c}-avatar"><@jive.userAvatar user=result size=22 useLinks=false /></span></label>
        </td>
        <td class="jive-table-cell-name">
            <label for="userChk-${result.ID?c}"><@jive.displayUserDisplayName user=result /></label>
        </td>
        <td class="jive-table-cell-email">
            <#if (result.email?has_content)>
                ${result.email?html}
            <#else>
                <@s.text name="profile.hidden.text" />
            </#if>
        </td>
        <#if (view == 'newest')>
            <td class="jive-table-cell-date">
                <@s.text name='search.user.result.joined'>
                <@s.param>${result.creationDate?date}</@s.param>
                </@s.text>
            </td>
        </#if>
         <#if (statusLevelEnabled && (view != 'newest'))>
            <td class="jive-table-cell-status">
                <#if community?exists>
                    <@jive.userStatusLevel user=result container=community />
                <#else>
                    <@jive.userStatusLevel user=result />
                </#if>
                &nbsp;
            </td>
        </#if>
    </tr>
</#if>
</#macro>

<#macro userFilter field values applied isQuery>
    <script type="text/javascript">filterIndexMap.f${field.ID?c} = '${filterIndex?c}';filterTypeMap.f${field.ID?c} = '${field.type.ID?c}'</script>
    <@s.hidden name="searchFilters[${filterIndex?c}].fieldID" value="${field.ID?c}"/>
    <div id="filter-table-row-${field.ID?c}" class="jive-people-browse-filters">

        <#if (!applied)><label for="searchFilters[${filterIndex?c}].value"><@s.text name="search.user.filter.byprofile" /> <@displayProfileFieldName field /><@s.text name="global.colon" /></label></#if>

        <div id="filter-${field.ID?c}">
         <#if (!applied)>
            <#-- BOOLEAN or MULTILIST or SINGLELIST -->
            <#if field.typeID == 1 || field.typeID == 7 || field.typeID == 8>
                <@s.select id="filter-field-${field.ID?c}" theme="simple" name="searchFilters[${filterIndex?c}].value" cssClass="filterOption" onchange="$j('#profilesearchform').submit()"
                    list=field.options listKey="value" listValue="value" emptyOption="true"/>
            <#-- DATETIME -->
            <#elseif field.typeID == 2>
                <table style="border-collapse:collapse;">
                <tr><td>
                <@s.datetimepicker theme="simple" id="filter-field-${field.ID?c}.minValue" cssClass="filterOption" size="10"
                    name="searchFilters[${filterIndex?c}].minValue"/>
                <ul id="acDiv-${filterIndex?c}-min" class="autocomplete"></ul>
                <span class="jive-description"><@s.text name="search.user.filter.min.text"/></span></td>
                <td><@s.datetimepicker theme="simple" id="filter-field-${field.ID?c}.maxValue"  cssClass="filterOption" size="10"
                    name="searchFilters[${filterIndex?c}].maxValue" />
                <ul id="acDiv-${filterIndex?c}-max" class="autocomplete"></ul>
                <span class="jive-description"><@s.text name="search.user.filter.max.text"/></span></td>
               </tr></table>
			 <p><input type="button" value="<@s.text name="search.user.filter.button" />" onclick="$j('#profilesearchform').submit()"/></p>
       <#-- DECIMAL or NUMBER -->
            <#elseif field.typeID == 3 || field.typeID == 5>

                <@s.textfield theme="simple" id="filter-field-${field.ID?c}.minValue"
                name="searchFilters[${filterIndex?c}].minValue" cssClass="filterOption"/>
                <ul id="acDiv-${filterIndex?c}-min" class="autocomplete"></ul>
                <span class="jive-description"><@s.text name="search.user.filter.min.text"/></span>
                <@s.textfield theme="simple" id="filter-field-${field.ID?c}.maxValue"
                name="searchFilters[${filterIndex?c}].maxValue" cssClass="filterOption"/>
                <ul id="acDiv-${filterIndex?c}-max" class="autocomplete"></ul>
                <span class="jive-description"><@s.text name="search.user.filter.max.text"/></span>
                <input type="button" value="<@s.text name="search.user.filter.button" />" onclick="$j('#profilesearchform').submit()"/>

            <#-- OTHER TEXT FIELDS -->
            <#elseif field.typeID == 9 || field.typeID == 10 || field.typeID == 12>

                <#if (values?exists)>
                    <select id="filter-field-${field.ID?c}" name="searchFilters[${filterIndex?c}].value" onchange="$j('#profilesearchform').submit()" class="filterOption">
                        <option value="">(${values?size} <@s.text name="search.form.options.link" />)</option>
                    <#list values as pfvc>
                        <option value="${pfvc.value.value?html}">${pfvc.value.value?html} (${pfvc.count?html})</option>
                    </#list>
                    </select>
                </#if>
            <#else>
                <@s.textfield theme="simple" id="filter-field-${field.ID?c}" name="searchFilters[${filterIndex?c}].value" cssClass="filterOption"/>
                <ul id="acDiv-${filterIndex?c}" class="autocomplete"></ul>
                <input type="button" value="<@s.text name="search.user.filter.button" />" onclick="$j('#profilesearchform').submit()"/>
            </#if>
             <#-- AUTOCOMPLETE ARRAY FOR ANY NON-SELECT AND NON-COMPLEX (ADDRESS, PHONE NUMBER) TYPE -->
             <#if (field.typeID != 1 && field.typeID != 7 && field.typeID != 8 && field.typeID != 9 && field.typeID != 10 && field.typeID != 11 && field.typeID != 12 && field.typeID != 14)>
                 <script type="text/javascript">
                 $j(document).ready(function() {
                 <#if (!hasResultKey)>acVals['${filterIndex?c}'] = [<#list values as pfvc><#if (pfvc_index > 0)>,</#if>"${pfvc.value.simpleValue?js_string}"</#list>];</#if>
                 <#if (field.typeID == 2 || field.typeID == 3 || field.typeID == 5)>
                   acFuncs['${filterIndex?c}'] = function(){
                       $j("[id='filter-field-${field.ID?c}.minValue']").suggest(acVals['${filterIndex?c}'], {
                            attachObject: $j('#acDiv-${filterIndex?c}-min')
                       });
                       $j("[id='filter-field-${field.ID?c}.maxValue']").suggest(acVals['${filterIndex?c}'], {
                            attachObject: $j('#acDiv-${filterIndex?c}-max')
                       });
                   }
                 <#elseif (field.typeID != 9 && field.typeID != 10 &&  field.typeID != 12)>
                   acFuncs['${filterIndex?c}'] = function(){
                       $j('#filter-field-${field.ID?c}').suggest(acVals['${filterIndex?c}'], {
                            attachObject: $j('#acDiv-${filterIndex?c}')
                       });
                   }
                 </#if>
                      convertSelectsIntoLinks();
                      for (func in acFuncs){
                          acFuncs[func]();
                      }
                 });
             </script>
             </#if>
        <#else>
            <#if (field.typeID == 2 || field.typeID == 3 || field.typeID == 5)>
                <input id="filter-field-${field.ID?c}.minValue" type="hidden" name="searchFilters[${filterIndex?c}].minValue" value="${appliedFilterMap.get(field).minValue!?html}"/>
                <input id="filter-field-${field.ID?c}.maxValue" type="hidden" name="searchFilters[${filterIndex?c}].maxValue" value="${appliedFilterMap.get(field).maxValue!?html}"/>
            <#else>
                <input id="filter-field-${field.ID?c}" type="hidden" name="searchFilters[${filterIndex?c}].value" value="${appliedFilterMap.get(field).value!?html}"/>
            </#if>
        </#if>
        </div>
    </div>

    <#assign filterIndex = filterIndex + 1>

</#macro>
