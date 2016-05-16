// "constants"
var DEFAULT_PERCENT = 95;
var MIN_SEQUENCE_LENGTH = 1;
var MAX_SEQUENCES = 100;

// global variables
var colorArray = [
	"d1ee31","b0e356","a6e356","9ce556","91e653","87e753","87e754","86e754","85e754","84e755",
	"83e755","82e755","81e756","81e756","80e756","7fe757","7ee657","7de658","7ce658","7be659",
	"7be659","7ae65a","79e65a","78e65b","77e65b","76e55c","75e55c","75e55d","74e55e","73e55e",
	"72e55f","71e560","70e560","70e461","6fe461","6ee462","6de463","6ce463","6ce464","6be465",
	"6ae466","69e466","68e367","68e368","67e368","66e369","65e36a","64e36b","64e36b","63e36c",
	"62e36d","61e36e","5fe370","5de372","5be374","5ae476","58e479","56e47b","55e57d","53e580",
	"52e582","50e584","4ee587","4de589","4be58b","4ae58d","49e58f","47e591","46e592","45e594",
	"43e595","41e497","40e298","3ee198","3ddf98","3cdd97","3bda97","39d797","38d497","36d097",
	"33cc99","2fc79c","2ac29f","24bca4","1db5a8","16aead","11a7b2","0ca0b6","0b9aba","0b93bc",
	"0b8cbd","197fbd","3071ba","4c63b5","6957ac","844c9e","9c4288","b63969","ce3249","e32c2c"];
var loadingHTML = '<div class="loading"><img src="images/loading.gif" alt="[Loading]" /><br />In progress...</div>';
var loadingHTMLtiny = '<img class="loading_tiny" src="images/loading.gif" alt="Loading..." style="width:15px;height:15px;" />';
var tabs;
var tabCounter = 1;
var tabData = new Array();
var stop = false;
var parseURL = true;

// entry point
$(function(){
	// for JS only css
	$('HTML').addClass('JS');

	// address handler
	$.address.init(function(event) {
		// tabs setup
		tabs = $("#tabs").tabs({
			tabTemplate: '<li><a href="#{href}" title="#{label}" rel="address:/#{href}">#{label}</a><span class="ui-icon ui-icon-close">Remove Tab</span></li>',
			panelTemplate: '<li>' + loadingHTML + '</li>',
			add: function(event, ui) {
				/* Result */
				var id = ui.panel.id;
				var idBase = id.replace(/[0-9]+$/, '');
				var resultPanel = $('#'+id);
				var multi = false;
				
				resultPanel.addClass('result');
				
				var sync = 0;
				for(var i=0; tabData[idBase+i] != null; i++){
					var tmpId = idBase+i;
					var sequence = tabData[tmpId]['formData']['sequence'];
					var other = tabData[tmpId]['formData']['other'];
					var loadPanel = $('#'+tmpId);
					sync++;
					
					if(i==1){
						multi = true;
						var globalOptions = 
							$('<div class="global_options theme_panel ui-corner-all">' +
								'<div class="global_options_text">'+loadingHTMLtiny+' Global options can be changed once all processing in finished.</div>'+
							'</div>');
						var downloadPanel =
							$('<div class="export_options theme_panel ui-corner-all">' +
								'<div class="export_options_text">'+loadingHTMLtiny+' Results can be exported once all processing in finished.</div>'+
							'</div>');
						
						resultPanel.prepend(globalOptions).append(downloadPanel);
					}
					
					var url = 'result.php?id='+tmpId+'&'+other;
					if(/^example/.test(id)) { 
						url = 'resultTut.php?id='+tmpId;
					}
					
					loadPanel.load(url, {'sequence' : sequence}, function(){
						sync--;
						if(sync == 0){
							addGlobalPanels(resultPanel, id, multi);
						}
						initResultPage($(this));
					});
				}
				
				function addGlobalPanels(resultPanel, id, multi){
					resultPanel.find(' > .export_options:first').remove().end().append(
						'<div class="export_options theme_panel ui-corner-all">' +
						'<form id="'+id+'export_form" class="export_form clear" action="export.php" method="post" enctype="multipart/form-data">' +
							'<input type="hidden" name="result_text" class="result_text" value="" />'+
							'<input type="radio" name="'+id+'_export_radio" id="'+id+'_radio_1" value="filtered" checked="checked" />'+
							'<label for="'+id+'_radio_1">Filtered</label>'+
							'<input type="radio" name="'+id+'_export_radio" id="'+id+'_radio_2" value="all" />'+
							'<label for="'+id+'_radio_2">All</label> '+
							'<div class="export_button_cont"><input type="submit" name="export_button" class="export_button" value="Download" /></div>'+
						'</form>' +
						'</div>'
					)
					.find("input:submit, input:reset, input:button").button().end()
					.find('form input:radio').customInput();
					
					if(multi){
						resultPanel.find(' > .global_options:first').html(
							'<div class="clear">' +
								'<div class="label css_left">Global Specificity: </div>' +
								'<div class="percent css_right" id="'+id+'_percent"><input type="text" id="'+id+'_percentinput" name="'+id+'_percentinput" value="95" maxlength="2" size="2" />%</div>' +
							'</div>' +
							'<div class="slider" id="'+id+'_slider"><img class="ui-corner-all" src="css/images/color-scale.png" alt="color scale" /></div>' +
							'<div class="clear tips"><div class="css_left"><input type="button" class="open_all_btn" value="Open All" /> <input type="button" class="collapse_all_btn" value="Close All" /></div>' +
							'<div class="css_left"><span class="ui-icon ui-icon-bullet"></span>Click headers to open or close results.</div>' +
							'<div class="css_left"><span class="ui-icon ui-icon-bullet"></span>Hold click and drag headers to rearrange results.</div></div>'
						)
						.find("input:submit, input:reset, input:button").button().end()
						.find('.slider').slider({
							max: 99,
							value: DEFAULT_PERCENT,
							range: 'min',
							animate: false,
							slide: function(event, ui){
								$(this).parent().find('.percent:first input:text:first').val(ui.value);
							},
							change: function(event, ui){
								glSlider = $(this);
								resultPanel.find('.slider').not(glSlider).each(function(){
									$(this).slider("option", "value", glSlider.slider("option", "value"));
								});
							}
						})
						.slider("option", "value", DEFAULT_PERCENT).end()
						.find('#'+id+'_percentinput').val(DEFAULT_PERCENT)
						.change(function(){
							var percentVal = $(this).val();
							
							if(!(percentVal >= 0) || percentVal == ''){ 
								percentVal = DEFAULT_PERCENT;
								$(this).val(DEFAULT_PERCENT);
							}
							resultPanel.find('.slider').each(function(){
								$(this).slider("option", "value", percentVal);
							});
						}).end()
						.find('input:button.global_options_button:first').click(function(e){
							resultPanel.find('.global_options_wrapper:first').slideToggle("fast");
						}).toggle(function(){
								$(this).attr('value', 'Hide Global Options');
							}, function(){
								$(this).attr('value', 'Show Global Options');
							}
						);
					}
				}
			},
			remove: function(event, ui){
				var id = ui.panel.id;
				var idBase = id.replace(/[0-9]+$/, '');
				
				for(var i=0; tabData[idBase+i] != null; i++){
					tabData[idBase+i] = null;
				}
			},
			load: function(event, ui){
				/* URL submission */
				if(parseURL) {
					var info = window.location.href;
					var start = info.indexOf('?');
					info = (start >= 0)? info.slice(start+1) : '';
					
					var sequences = '';
					var otherFormData = '';
					var accType = '';
					var accText = '';
					
					if(info.length > 0) {
						var data = info.split('&');
						
						var i;
						for(i=0; i < data.length; i++){
							if(/^acc=.*:.*$/i.test(data[i])) {
								var acc = data[i].split('=')[1];
								accType = acc.split(':')[0];
								accText = acc.split(':')[1];
							} else if(/^model=.*$/i.test(data[i])) {
								otherFormData = data[i].split('=')[1];
							} else if(/^seq=.*$/i.test(data[i])) {
								sequences = data[i].split('=')[1].toUpperCase();
							}
						}
					}
					
					if(((accType.length > 0 && accText.length > 0) || sequences.length > 0) && otherFormData.length > 0) {
						sequences = $.trim(sequences).split('>');
						otherFormData = otherFormData.split(';');
						
						var i;
						for(i = 0; i < otherFormData.length; i++) {
							if(accText.length == 0) {
								createResultPageFromSeq(sequences, 'organism=' + otherFormData[i], '');
							} else {
								accText = (''+accText).split(',');
								createResultPageFromAcc(accType, accText, 'organism=' + otherFormData[i], sequences);
							}
						}
					}
				}
				parseURL = false;
				/* ************* */
			
				$("input:submit, input:reset, input:button").button(); // style jQuery UI elements
				$('textarea.resizable:not(.processed)').TextAreaResizer(); // init resizable textarea
				$('form input:checkbox').customInput(); // custom radio/checkbox
				$('#accession_text, #sequence').autocomplete({ // Both Uniprot and Sequence input alert
					delay: 0,
					minLength: 0,
					search: function(event, ui) {
						showAccSeqAlert();
						return false;
					}
				});
				
				//$.address.value(ui.tab.rel.replace('address:/', '')); // change url
			},
			select: function(event, ui) {
				//$.address.value(ui.tab.rel.replace('address:/', ''));
			},
			ajaxOptions: {
				error: function(xhr, status, index, anchor) {
					$(anchor.hash).html("Couldn't load this tab. We'll try to fix this as soon as possible.");
				}
			},
			fx: { height: ['toggle', 'swing'] },
			cache: true
		})
		.css('display', 'block');
	}).change(function(event) {
		// set page title
		//$.address.title($.address.title().split(' | ')[0] + ' | ' + $('a').filter('[rel=address:' + event.value + ']').text());
	}).externalChange(function(event) {
		// select the proper tab
		tabs.tabs('select', $('a').filter('[rel=address:' + event.value + ']').attr('href'));
	});
	
	// close icon: remove tab on click
	$('#tabs span.ui-icon-close').live('click', function() {
		var index = $('li',tabs).index($(this).parent());
		tabs.tabs('remove', index);
	});
	
	// fade legend on mouseover
	$('canvas.jqplot-event-canvas').live('mousemove mouseout', function(e) {
		var legend = $(this).siblings('table.jqplot-table-legend');
		var pos = legend.offset();
		
		if(e.pageX >= pos.left && e.pageY <= pos.top+legend.height() && e.pageX <= pos.left+legend.width() && e.pageY >= pos.top){
			if(!legend.hasClass('legend-fade')){
				legend.addClass('legend-fade');
				legend.stop().animate({opacity: 0.2});
			}
		} else if(legend.hasClass('legend-fade') || e == 'mouseout') {
			legend.removeClass('legend-fade');
			legend.stop().animate({opacity: 1});
		}
	});
	
	// fill textarea with an example of FASTA format
	$('#fasta_example, #uniprot_example').live('click', function(e){
		e.preventDefault();
		var $this = $(this);
		$.ajax({
			url : $this.attr('href'),
			success : function (data) {
				$this.parent().find(':text, textarea').val(data);
				showAccSeqAlert();
			}
		});
	});
	
	// open tutorial or service page
	$('#tutorial_link, #tutorial_corner_link, #service_corner_link').live('click', function(e){
		e.preventDefault();
		var $this = $(this);
		var newTabIndex = tabs.tabs( "length" );
		tabs.tabs('add', $this.attr('href'), $this.attr('title'));
		tabs.tabs("option", "selected", newTabIndex);
	});
	
	// open Example output pages
	$('#output_single_example').live('click', function(e){
		e.preventDefault();
		var $this = $(this);
		
		tabData['example_output_0'] = ['formData', 'plotData', 'tableData'];
		tabData['example_output_0']['formData'] = {'sequence' : '', 'other' : ''};
		
		var newTabIndex = tabs.tabs( "length" );
		tabs.tabs('add', '#example_output_0', "Example Single Output");
		tabs.tabs("option", "selected", newTabIndex);
	});
	$('#output_mutliple_example').live('click', function(e){
		e.preventDefault();
		var $this = $(this);
		
		tabData['example2_output_0'] = ['formData', 'plotData', 'tableData'];
		tabData['example2_output_0']['formData'] = {'sequence' : '', 'other' : ''};
		tabData['example2_output_1'] = ['formData', 'plotData', 'tableData'];
		tabData['example2_output_1']['formData'] = {'sequence' : '', 'other' : ''};
		tabData['example2_output_2'] = ['formData', 'plotData', 'tableData'];
		tabData['example2_output_2']['formData'] = {'sequence' : '', 'other' : ''};
		
		var oldTemplate = tabs.tabs("option", "panelTemplate");
		newTemplate = '<li><div class="sortable"><div class="accordion"><h3><a href="#">(1) sequence 1</a></h3><div id="example2_output_0" class="accordionPanel"><div class="loading"><img src="images/loading.gif" alt="[Loading]" /><br />In progress...</div></div></div><div class="accordion"><h3><a href="#">(2) sequence 2</a></h3><div id="example2_output_1" class="accordionPanel"><div class="loading"><img src="images/loading.gif" alt="[Loading]" /><br />In progress...</div></div></div><div class="accordion"><h3><a href="#">(3) sequence 3</a></h3><div id="example2_output_2" class="accordionPanel"><div class="loading"><img src="images/loading.gif" alt="[Loading]" /><br />In progress...</div></div></div></div></li>';
		
		tabs.tabs("option", "panelTemplate", newTemplate);
		
		var newTabIndex = tabs.tabs( "length" );
		tabs.tabs('add', '#example2_output_', "Example Multiple Output");
		tabs.tabs("option", "selected", newTabIndex);
		
		tabs.tabs("option", "panelTemplate", oldTemplate);
		
		// accordion handlers
		$('#'+'example2_output_'+' div.accordion').accordion({
			autoHeight: false,
			animated: true,
			collapsible: true,
			active: false
		})
		.find('h3 a').click(function( event ) {
			if ( stop ) {
				event.stopImmediatePropagation();
				event.preventDefault();
				stop = false;
			}
		}).end()
		.eq(0).accordion("option", "active", 0);
		
		$('#'+'example2_output_'+' .sortable').sortable({
			axis: "y",
			handle: "h3",
			stop: function() {
				stop = true;
			}
		});
	});
	
	// open all button
	$('.open_all_btn').live('click', function(e){
		e.preventDefault();
		var resultCont = $(this).closest('.result');
		resultCont.find('div.accordion').each(function(){
			var $this = $(this);
			if(!$this.find('h3:first').hasClass('ui-state-active'))
				$this.accordion("option", "active", 0);
		});
	});
	
	// collapse all button
	$('.collapse_all_btn').live('click', function(e){
		e.preventDefault();
		var resultCont = $(this).closest('.result');
		resultCont.find('div.accordion').each(function(){
			var $this = $(this);
			if($this.find('h3:first').hasClass('ui-state-active')) {
				$this.accordion("option", "active", 0)
				.find('h3:first').removeClass('ui-state-focus');
			}
		});
	});
	
	/* Download Text */
	$('.result .export_button').live('click', function(e){
		var resultCont = $(this).closest('.result');
		var idBase = resultCont.attr('id').replace(/[0-9]+$/, '');
		var resultExport = resultCont.find('.export_form:first');
		var result_text_cont = resultExport.find('input:hidden.result_text:first');
		var download_option = resultExport.find('input:radio:checked:first').val();
		var numTables = resultCont.find('table.data').length;
		var result_text = '';
		
		for(var i=0; i<numTables; i++){
			var resultTable = tabData[idBase+i]['tableData'];
			var resultHeader = $('#'+idBase+i).find('.header > .headerText:first');
			updateTable(resultTable);
			convertTableToText(resultTable, resultHeader);
		}
		
		result_text_cont.val(result_text);
		
		function convertTableToText(resultTable, resultHeader){
			var nNodes;
			
			if(download_option == 'filtered') nNodes = resultTable.fnGetFilteredNodes();
			else if (download_option == 'all') nNodes = resultTable.fnGetNodes();
			
			if(nNodes != null){
				result_text += resultHeader.text()+"\n";
				for(var i=0; i<nNodes.length; i++){
					var row = $(nNodes[i]);
					row.find('td').each(function(){
						result_text += $(this).text()+"\t";
					});
					result_text += "\n";
				}
				result_text += "\n";
			}
		}
	});
	
	/* Clear Bioform */
	$('#bioform-clear').live('click', function(e){
		clearMessage();
		return true;
	});
	
	/* Submit Bioform */
	$('#bioform-submit').live('click', function(e){
		e.preventDefault();
		
		var sequences = $.trim($('#bioform #sequence').val()).split('>');
		var otherFormData = $('#bioform :input').not('#sequence').fieldSerialize();
		var accType = $('#accession_type').val();
		var accText = $('#accession_text').val();
		
		if(accText.length == 0) {
			createResultPageFromSeq(sequences, otherFormData, '');
		} else {
			accText = accText.split(',');
			createResultPageFromAcc(accType, accText, otherFormData, sequences);
		}
	});
});

function createResultPageFromAcc(aType, aText, other, seq){
	var accType = aType;
	var accTextArray = aText;
	var otherFormData = other;
	var sequences = seq;
	
	var errMessage = '';
	var accTextContainer = $('#accession_text');
	var accSeqs = [''];
	var sync = 0;
	
	for(var i=0; i<accTextArray.length; i++) {
		var accText = $.trim(accTextArray[i]);
		if(accType == 'uniprot' && accText.length > 0){
			sync++;
			if(sync == 1) {
				accTextContainer.parent().find(' > .loading_tiny:first').remove().end().append(loadingHTMLtiny);
			}
			$.get('uniprot.php?a='+accText, function(data) {
				if(!(data == '' || data == null)){
					accSeqs.push(data);
				} else {
					errMessage = "UniProt code was not found.";
				}
				sync--;
				if(sync == 0) {
					accTextContainer.siblings('.loading_tiny').remove();
					if(sequences.length > 0){
						if(!sequences[0].length >= MIN_SEQUENCE_LENGTH) sequences.shift();
						else sequences[0] = "sequence\n"+sequences[0];
					}
					accSeqs = accSeqs.concat(sequences);
					createResultPageFromSeq(accSeqs, otherFormData, errMessage);
				}
			});
		}
	}
}

function createResultPageFromSeq(seq, other, errMsg){
	var tabNum = tabCounter++;
	var newTabIndex = tabs.tabs( "length" );
	var errMessage = (errMsg != null)? errMsg : '';
	
	var sequences = seq;
	var otherFormData = other;
	
	var numSequences;
	var id;
	if(!sequences[0].length >= MIN_SEQUENCE_LENGTH) sequences.shift();
	else sequences[0] = "sequence\n"+sequences[0];
	numSequences = sequences.length > MAX_SEQUENCES? MAX_SEQUENCES : sequences.length;
	
	if(numSequences == 1){
		addTabData(tabNum, 0, '>'+sequences[0]);
		addTab(tabNum, 0);
	} else if(numSequences > 1){
		clearMessage();
		
		var tabId = 'result_'+tabNum+'_';
		var oldTemplate = tabs.tabs("option", "panelTemplate");
		
		// create new panel template
		var newTemplate = '<li><div class="sortable">';
		for(var i=0; i<numSequences; i++){
			var header = ''; var j;
			var tmpId = tabId+i;
			
			newTemplate += '<div class="accordion">';
			header = ((j = sequences[i].indexOf(';')) > -1)? sequences[i].substring(0, j) : sequences[i].split("\n", 1)[0];
			newTemplate += '<h3><a href="#">('+(i+1)+') '+header+'</a></h3><div id="'+tmpId+'" class="accordionPanel">'+loadingHTML+'</div>';
			addTabData(tabNum, i, '>'+sequences[i]);
			newTemplate += '</div>';
		}
		newTemplate += '</div></li>';
		
		tabs.tabs("option", "panelTemplate", newTemplate);
		addTab(tabNum, '');
		tabs.tabs("option", "panelTemplate", oldTemplate);
		
		// accordion handlers
		$('#'+tabId+' div.accordion').accordion({
			autoHeight: false,
			animated: true,
			collapsible: true,
			active: false
		})
		.find('h3 a').click(function( event ) {
			if ( stop ) {
				event.stopImmediatePropagation();
				event.preventDefault();
				stop = false;
			}
		}).end()
		.eq(0).accordion("option", "active", 0);				
		$('#'+tabId+' .sortable').sortable({
			axis: "y",
			handle: "h3",
			stop: function() {
				stop = true;
			}
		});
	}
	
	if(numSequences <= 0) {
		errMessage += "There is no input to process. Input an accession or sequence.";
	}
	
	showError(errMessage);
	
	function addTabData(tabNum, resultNum, sequence){
		clearMessage();
		
		var id = 'result_'+tabNum+'_'+resultNum;
		tabData[id] = ['formData', 'plotData', 'tableData'];
		tabData[id]['formData'] = {'sequence' : sequence, 'other' : otherFormData};
	}
	function addTab(tabNum, resultNum){
		var id = 'result_'+tabNum+'_'+resultNum;
		tabs.tabs('add', '#'+id, 'Result '+tabNum);
		tabs.tabs("option", "selected", newTabIndex);
	}
}

// error messages
function clearMessage(){
	$('#bioform-message').stop(true, true).slideUp();
}
function showError(errMessage){
	if(errMessage.length > 0){
		var messages = errMessage.split('.');
		errMessage = '<ul>';
		for(var i = 0; i < messages.length - 1; i++) {
			errMessage += '<li>' + messages[i] + '</li>';
		}
		errMessage += '</ul>';
		$('#bioform-message').html('<div class="ui-state-error ui-corner-all"><p><span class="ui-icon ui-icon-alert"></span><strong>Error:</strong> ' + errMessage + '</p></div>');
		if(!$('#bioform-message').is(':visible'))
		$('#bioform-message').stop(true, true).slideDown();
	}
}
function showAlert(warnMessage){
	if(warnMessage.length > 0){
		var messages = warnMessage.split('.');
		warnMessage = '<ul>';
		for(var i = 0; i < messages.length - 1; i++) {
			warnMessage += '<li>' + messages[i] + '</li>';
		}
		warnMessage += '</ul>';
		$('#bioform-message').html('<div class="ui-state-highlight ui-corner-all"><p><span class="ui-icon ui-icon-info"></span><strong>Alert:</strong> ' + warnMessage + '</p></div>');
		$('#bioform-message').stop(true, true).slideDown();
	}
}
function showAccSeqAlert(){
	if($('#accession_text').val() != '' && $('#sequence').val() != '') {
		showAlert('There is input in both the accession and sequence fields. Both will be processed.');
	} else {
		clearMessage();
	}
} 

function initResultPage(resultCont){
	var id = resultCont.attr('id');
	var resultOptions 	= resultCont.find('#'+id+'_options');
	var resultHeader 	= resultCont.find('#'+id+'_header');
	var resultGraph 	= resultCont.find('#'+id+'_graph');
	var resultPercent 	= resultCont.find('#'+id+'_percent > input:text:first');
	var resultSlider 	= resultCont.find('#'+id+'_slider');
	var resultTable 	= resultCont.find('#'+id+'_data');
	
	var seqTextCont = resultHeader.find('.seqText:first');
	var isSingle = resultCont.hasClass('ui-tabs-panel');
	
	/* Options */
	if(isSingle) {
		resultOptions.find('.option_slider').attr('checked', 'checked');
		resultSlider.parents('.slider_wrapper').show();
	}
	
	resultOptions
	.find('.option_sequence').change(function(e){
		resultHeader.parent().slideToggle("fast");
	}).end()
	.find('.option_graph').change(function(e){
		resultGraph.parent().slideToggle("fast");
		if(!resultTable.is(':visible')) {
			updateTable(resultTable);
		}
		updateGraph(resultGraph, resultTable);
	}).end()
	.find('.option_slider').change(function(e){
		resultSlider.parent().slideToggle("fast");
	}).end()
	.find('.option_table').change(function(e){
		resultTable.closest('.data_wrapper').slideToggle("fast");
		updateTable(resultTable);
	}).end()
	.find('input:checkbox').customInput();
	
	
	/* Sequence */
	var charPerLine = 60;
	var lineNumText = '';
	var seqText = seqTextCont.html();
	for(var i=0, n=1, bl=0, al=0; i<seqText.length; i+=(al-bl)+1){
		var c = seqText.charAt(i);
		if(/^<$/.test(c)){
			var tagEnd = seqText.indexOf('>', i);
			if(tagEnd != -1) i = tagEnd;
			c = seqText.charAt(i);
		}
		bl=seqText.length;
		if(/^[A-Z]$/.test(c)){
			seqText=seqText.replaceAt(i, '<span>'+c+'</span>'+((n%10 == 0)? ' ' : '')+((n%charPerLine == 0)? "\n<br />" : ''));
			if(n%charPerLine == 1) lineNumText += n+'<br />';
			n++;
		}
		al=seqText.length;
	}
	seqTextCont.html(seqText);
	resultHeader.find('.lineNum:first').html(lineNumText);
	
	/* Table / Sequence */
	var seqSpans = resultHeader.find('.seqText:first span');
	resultTable.find('tbody tr').hover(function(){
		$(this).addClass('highlighted');
	}, function(){
		$(this).removeClass('highlighted');
	}).each(function(){
		var $this = $(this);
		var position = $this.find('.position:first');
		var specificity = $this.find('.specificity:first');
		var specNum = Math.round(parseFloat(specificity.text().replace('%','')));
		var color = '#'+colorArray[specNum-1];
		$this.find('.sequence:first span.aminoChar').css('background-color', color); // color table amino
		seqTextCont.find('span:eq('+(parseInt(position.text())-1)+')')
			.addClass('amino nocolor ui-corner-all')
			.css('background-color', color) // color header amino
			.attr('title', position.text()+', '+specificity.text())
			.attr('specificity', specificity.text().replace('%',''))
			.tooltip({
				track: true,
				delay: 0,
				showURL: false,
				showBody: " - ",
				fade: 0
			});
	}).end()
	.dataTable({
		"bAutoWidth": false,
		"bFilter": false,
		"bInfo": true,
		"bJQueryUI": true,
		"bLengthChange": true,
		"bPaginate": true,
		"bSort": true,
		"bSortClasses": true,
		"sPaginationType": "full_numbers",
		"aaSorting": [[ 4, "desc" ]]
	});
	tabData[id]['tableData'] = resultTable;
	
	/* Slider */
	resultSlider.slider({
		max: 99,
		value: DEFAULT_PERCENT,
		range: 'min',
		animate: false,
		slide: function(event, ui){
			resultPercent.val(ui.value);
		},
		change: function(event, ui){
			resultPercent.val(ui.value);
			
			// update sequence aminos
			seqTextCont.find('.amino').each(function(){
				$this = $(this);
				if($this.attr('specificity') >= ui.value)
					$this.removeClass('nocolor');
				else $this.addClass('nocolor');
			});
			
			if(resultTable.is(':visible') || resultGraph.is(':visible')) {
				updateTable(resultTable);
			}
			
			if(resultGraph.is(':visible')) {
				updateGraph(resultGraph, resultTable);
			}
		}
	})
	.slider("option", "value", DEFAULT_PERCENT);
	
	/* Percent */
	resultPercent.val(DEFAULT_PERCENT)
	.change(function(){
		var percentVal = $(this).val();
		
		if(!(percentVal >= 0) || percentVal == ''){ 
			percentVal = DEFAULT_PERCENT;
			$(this).val(DEFAULT_PERCENT);
		}
		resultSlider.slider("option", "value", percentVal);
	});
	
	/* Graph */
	updateGraph(resultGraph, resultTable);
}

function updateGraph(resultGraph, resultTable){
	var dataArray = []; // array of datasets
	var seriesArray = []; // array of series styles
	var aminosStr = ''; // string of already found aminos
	var shapes = ["filledCircle", "filledSquare", "filledDiamond", "circle", "square", "diamond"];
	var shapeCount = -1;

	// get graph data
	var nFilteredNodes = resultTable.fnGetFilteredNodes();
	for(var i=0; i<nFilteredNodes.length; i++){
		var row = $(nFilteredNodes[i]);
		var aminoAcid = row.find('.aminoAcid').text();
		var datax = parseFloat(row.find('.position').text());
		var datay = parseFloat(row.find('.specificity').text().replace('%',''));
		
		if(aminosStr.search(aminoAcid) < 0) {
			aminosStr += aminoAcid; // add newly found amino
			dataArray.push([]); // add array for new amino
			
			// add series style for new amino
			shapeCount = (shapeCount+1)%shapes.length;
			var newSeries = {label: aminoAcid, showLine:false, markerOptions:{style:shapes[shapeCount]}};
			seriesArray.push(newSeries);
		}
		
		// add data point
		dataArray[aminosStr.search(aminoAcid)].push([datax, datay]);
	}
	
	resultGraph.empty();
	
	// check if data array is empty
	if (dataArray.length <1) {
		alert("Error: no data found");
		return;
	}
	
	// draw graph
	tabData[resultGraph.attr('id').replace(/_graph$/,"")]['plotData'] = $.jqplot(resultGraph.attr('id'), dataArray, {
		height: 300,
		width: 600,
		seriesColors: [ "#4bb2c5", "#EAA228", "#84c93a", "#579575", "#839557", "#958c12",
        "#953579", "#4b5de4", "#d8b83f", "#ff5800", "#0085cc"],
		series: seriesArray,
		cursor: {show:true, zoom:false, showTooltip:false},
		legend: {show: true, location: 'ne'},
		axes:{
			xaxis:{
				min: 1,
				label:'Position',
				autoscale: true,
				tickOptions:{formatString:'%d'},
				labelRenderer: $.jqplot.CanvasAxisLabelRenderer
			},
			yaxis:{
				max: 100,
				pad: 1.08,
				label:'Specificity (%)',
				autoscale: true,
				tickOptions:{formatString:'%d'},
				labelRenderer: $.jqplot.CanvasAxisLabelRenderer
			}
		},
		highlighter: {
			sizeAdjust: 6,
			useAxesFormatters: false,
			tooltipFormatString: '%P',
			tooltipLocation: 'se',
			fadeTooltip: false
		}
	});
}

function redrawGraph(panelId) {
	if($('#'+panelId+'_graph').length > 0){
		var plot = tabData[panelId]['plotData'];
		if(plot._drawCount == 0) {
			plot.replot();
		}
	}
}

function updateTable(resultTable) {
	var nNodes = resultTable.fnGetNodes();
	var specificity = resultTable.closest('.result_wrapper').find('.slider:first').slider("option", "value");

	for(var i=0; i<nNodes.length; i++){
		var row = $(nNodes[i]);
		var specificityCompare = parseFloat(row.find('.specificity:first').text().replace('%','')) >= specificity;
		
		if(!row.hasClass('hidden') && !specificityCompare){
			row.addClass('hidden');
			resultTable.fnDeleteRow(resultTable.fnGetPosition(nNodes[i]));
		} else if(row.hasClass('hidden') && specificityCompare){
			row.removeClass('hidden');
			var oSettings = resultTable.fnSettings();
			oSettings.aiDisplayMaster.push(i);
			resultTable.fnDraw();
		}
	}
}

String.prototype.replaceAt=function(index, str) {
	return this.slice(0, index) + str + this.slice(index+1);
}