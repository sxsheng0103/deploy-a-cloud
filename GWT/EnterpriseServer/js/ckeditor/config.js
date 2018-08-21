/**
 * @license Copyright (c) 2003-2015, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
     config.extraPlugins = 'lineheight';
      //将回车键和shift键调换，缩小行间距
     config.enterMode = CKEDITOR.ENTER_BR;
		 config.shiftEnterMode = CKEDITOR.ENTER_P;
	 
	
};
