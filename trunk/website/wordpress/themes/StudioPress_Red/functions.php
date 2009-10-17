<?php

if ( function_exists('register_sidebar') )
    register_sidebar(array(
	'name'=>'sidebar1',
        'before_widget' => '<li id="%1$s" class="sidebaritem">',
        'after_widget' => '</li>',
        'before_title' => '<h2 class="widgettitle">',
        'after_title' => '</h2>',
    ));
    register_sidebar(array(
	'name'=>'sidebar2',
        'before_widget' => '<li id="%1$s" class="sidebaritem">',
        'after_widget' => '</li>',
        'before_title' => '<h2 class="widgettitle">',
        'after_title' => '</h2>',
    ));

add_action('admin_menu', 'add_welcome_interface');

function add_welcome_interface() {
  add_theme_page('welcome', 'Theme Options', '8', 'functions', 'editoptions');
  }

function editoptions() {
  ?>
  <div class='wrap'>
  <h2>Theme Options</h2>
  <form method="post" action="options.php">
  <?php wp_nonce_field('update-options') ?>
  <p>Greeting:</p>
  <p><input type="text" name="greeting" value="<?php echo get_option('greeting'); ?>" /></p>
  <p>Welcome Message:</p>
  <p><textarea name="welcomemessage" cols="100%" rows="10"><?php echo get_option('welcomemessage'); ?></textarea></p>
  <p><input type="submit" name="Submit" value="Update Options" /></p>
  <input type="hidden" name="action" value="update" />
  <input type="hidden" name="page_options" value="greeting,welcomemessage" />
  </form>
  </div>
  <?php
  }

?>