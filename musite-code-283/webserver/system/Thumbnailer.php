<?php

/**
 * Thumbnailer (c) Practical thumb generator for WWW purposes.
 * Visit my site to download the latest release at http://freelogic.pl/thumbnailer/
 * If you have any questions please contact me at michal@ekochemia.pl
 * 
 * This script requires GD2 Library as also PHP5.
 * (PHP5.1.0 recommended)
 * 
 * Permission is hereby granted, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * @author Michal Zukowski (michael@freelogic.pl)
 * @license http://freelogic.pl/thumbnailer/license/
 * @version 1.2.5.2
 */

class ThumbnailerException extends Exception {}

class Thumbnailer
{
	const ROUND_TOP_LEFT=1;	// draw top left corner
	const ROUND_TOP_RIGHT=2;	// draw top right corner
	const ROUND_BOTTOM_LEFT=4;	// draw top bottom left corner
	const ROUND_BOTTOM_RIGHT=8;	// draw bottom right corner
	const ROUND_ALL=15;	// draw all corners
	
	/**
	 * Photo filename
	 *
	 * @var string
	 */
	public $name;
	
	/**
	 * Original photo data
	 *
	 * @var resource
	 */
	public $resource;
	
	/**
	 * Temporary photo data
	 * 
	 * @var tmp
	 * @since 1.2.0
	 */
	protected $tmp=array();
	
	/**
	 * Resource holder for the thumb
	 *
	 * @var resource
	 */
	public $thumb;
	
	/**
	 * Thumb width
	 *
	 * @var int
	 */
	public $thumb_width;
	
	/**
	 * Thumb height
	 *
	 * @var int
	 */
	public $thumb_height;
	
	/**
	 * Save function
	 *
	 * @var string
	 * @since 1.2.0
	 */
	public $savefunc;
	
	/**
	 * Default save location for saved thumbs.
	 * 
	 * @var string
	 * @since 1.2.1
	 */
	public static $saveLocation;
	
	/**
	 * Prefix for generated thumb's name. Applies only for auto-saving
	 * (when $saveLocation is set)
	 * 
	 * @var string
	 * @since 1.2.1
	 */
	public static $prefix='thumb_';
	
	/**
	 * Thumb's filename.
	 *
	 * @var string
	 */
	public $thumbName;
	
	/**
	 * Thumb's filesize.
	 */
	public $filesize;
	
	/**
	 * Which mime types of images are allowed.
	 * 
	 * Define which mime types of images are allowed.
	 * If it's empty - all mime types are allowed.
	 *
	 * @var array
	 * @since 1.2.5
	 */
	public static $allowed=array('image/jpeg','image/jpg','image/gif','image/png');
	
	/**
	 * Create new object of thumbnailer.
	 *
	 * @param string $file Photo filename
	 */
	public function __construct($file)
	{
		$this->prepare($file);
	}
	
	/**
	 * Prepare the object after the constructor being called.
	 *
	 * If you need more actions to be taken inside the constructor
	 * override this method.
	 * 
	 * @param string $file Photo filename
	 * @throws ThumbnailerException
	 * @since 1.2.0
	 */
	protected function prepare($file)
	{
		if (!file_exists($file)) {
			throw new ThumbnailerException('The photo does not exists', 7);
		}
		
		$this->name=realpath($file);
		$this->resource=$this->imagecreate($file);
	}
	
	/**
	 * Release resources if necessary
	 * @since 1.0.0
	 */
	public function __destruct()
	{
		// if the default location for saved thumbs
		// been set, save the thumbs there
		if (self::$saveLocation) {	
			$filename=$this->thumbName ? $this->thumbName : self::$prefix.$this->getFilename();
			$this->save(self::$saveLocation.DIRECTORY_SEPARATOR.$filename);
		}
		
		if (is_resource($this->thumb)) {
			@imagedestroy($this->thumb);
		}
		if (is_resource($this->resource)) {
			@imagedestroy($this->resource);
		}
		if (count($this->tmp)>0) {
			foreach ($this->tmp as $img) {
				if (is_resource($img)) {
					@imagedestroy($img);
				}
			}
		}
	}
	
	/**
	 * Check if photo has valid mime type.
	 *
	 * @param string $type Mime type
	 * @return bool If photo is valid
	 * @since 1.2.2
	 */
	public function isValid($type)
	{
		$info=@getimagesize($this->name);
		return !(empty($info) || $info[2] != $type);
	}
	
	/**
	 * Generate a symmetric thumb of an image.
	 *
	 * @param int Thumb size
	 * @since 1.0.0
	 * @throws ThumbnailerException
	 * @return Thumbnailer Self-reference for chaining
	 */
	public function &thumbSymmetric($width=100)
	{
		$old_width=imagesx($this->resource);
		$old_height=imagesy($this->resource);
		
		if (($width > $old_width) && ($width > $old_height))
			throw new ThumbnailerException('Requested thumb size exceeds photo size',1);
		
		// if photo's height is greater than the width
		if ($old_height > $old_width)
		{
			$width_percent=$width * 100 / $old_width;
			$new_height=($width_percent * $old_height) / 100;
			
			$this->thumb=imagecreatetruecolor($width, $new_height);
			$this->im($this->thumb, $this->resource, 0, 0, 0, 0, $width, $new_height, $old_width, $old_height);
			
			$this->thumb_height=$new_height;
			$this->thumb_width=$width;
		}
		// if photo's width is greater than the height
		elseif ($old_height < $old_width)
		{
			$height_percent=$width * 100 / $old_height;
			$new_width=$height_percent * $old_width / 100;
			
			$this->thumb=imagecreatetruecolor($new_width, $width);
			$this->im($this->thumb, $this->resource, 0, 0, 0, 0, $new_width, $width, $old_width, $old_height);
			
			$this->thumb_height=$width;
			$this->thumb_width=$new_width;
		}
		// if square
		else
		{
			$this->thumb=imagecreatetruecolor($width, $width);
			$this->im($this->thumb, $this->resource, 0, 0, 0, 0, $width, $width, $old_width, $old_height);
			
			$this->thumb_height=$width;
			$this->thumb_width=$width;
		}
		
		return $this;
	}
	
	/**
	 * Generate a square thumb of an image.
	 *
	 * @param int Height and width of the thumb
	 * @since 1.0.0
	 * @return Thumbnailer Self-reference for chaining
	 */
	public function &thumbSquare($size=100)
	{
		// firstly we create a symmetric thumb
		$this->thumbSymmetric($size);

		// then cut a square from it
		//
		// if photo's height is greater than the width
		if ($this->thumb_height > $this->thumb_width)
		{
			$y1=floor(($this->thumb_height-$size)/2);
			
			$tmp=$this->createTmpImage($size, $size);
			$this->im($tmp, $this->thumb, 0, 0, 0, $y1, $size, $size, $size, $size);
			$this->thumb=$tmp;
		}
		// if photo's width is greater than the height
		elseif ($this->thumb_height < $this->thumb_width)
		{
			$x2=floor(($this->thumb_width-$size)/2);
			
			$tmp=$this->createTmpImage($size, $size);
			$this->im($tmp, $this->thumb, 0, 0, $x2, 0, $size, $size, $size, $size);
			$this->thumb=$tmp;
		}
		
		$this->thumb_height=$size;
		$this->thumb_width=$size;
		return $this;
	}

	/**
	 * Generate a thumb with fixed width and height.
	 *
	 * @param int $width Thumb width
	 * @param int $height Thumb height
	 * @return Thumbnailer Self-reference for chaining
	 * @since 1.2.0
	 */
	public function &thumbFixed($width, $height)
	{		
		if ($width==$height) {
			$this->thumbSquare($width);
			return $this;
		}

		$img_width=imagesx($this->resource);
		$img_height=imagesy($this->resource);
		$this->thumb_width=$width;
		$this->thumb_height=$height;
		$thumb_ratio=$this->thumb_width/$this->thumb_height;
		
		// working out image's ratio
		if (($img_height*$width/$img_width) <= $height) {
			// zostanie po bokach
			$scaled_width=round($img_height*$thumb_ratio);
			$scaled_height=$img_height;
			$src_x=$img_width/2-$scaled_width/2;
			$src_y=0;
		}
		else {
			// zostanie na gorze
			$scaled_width=$img_width;
			$scaled_height=round($img_width/$thumb_ratio);
			$src_x=0;
			$src_y=$img_height/2-$scaled_height/2;
		}
		
		$tmp=$this->createTmpImage($scaled_width,$scaled_height);
		$this->im($tmp,$this->resource,0,0,$src_x,$src_y,$scaled_width,$scaled_height,$scaled_width,$scaled_height);		
		$this->thumb=imagecreatetruecolor($width,$height);
		$this->im($this->thumb,$tmp,0,0,0,0,$width,$height,$scaled_width,$scaled_height);
		
		return $this;
	}
	
	/**
	 * Rounds thumbnail corners with given parameters.
	 * Since 1.2.3 improved with correct size and smoothness.
	 *
	 * @param int Corners to round
	 * @param array Color defined as array(R,G,B)
	 * @param int Corner radius in pixels
	 * @since 1.1.0
	 * @return Thumbnailer Self-reference for chaining
	 */
	public function &round($corners=Thumbnailer::ROUND_ALL, $color=array(255,255,255), $corner_radius=5)
	{
		if (empty($this->thumb))
		{
			$this->thumb=$this->imagecreate($this->name);
			$this->thumb_width=imagesx($this->thumb);
			$this->thumb_height=imagesy($this->thumb);
		}
		
		$tyl=$this->createTmpImage($this->thumb_width, $this->thumb_height);
		imagecopymerge($tyl, $this->thumb, 0, 0, 0, 0, $this->thumb_width, $this->thumb_height, 100);
		$startx=$this->thumb_width*2-1;
		$starty=$this->thumb_height*2-1;
		$im_temp=$this->createTmpImage($startx,$starty);
		imagecopyresampled($im_temp, $tyl, 0, 0, 0, 0, $startx, $starty, $this->thumb_width, $this->thumb_height);
		
		$bg=imagecolorallocate($im_temp, $color[0], $color[1], $color[2]);
		$startsize=$corner_radius*3-1;
		$arcsize=$startsize*2+1;
		
		if ($corners & self::ROUND_TOP_LEFT) {
			imagearc($im_temp, $startsize, $startsize, $arcsize, $arcsize, 180,270,$bg);
			imagefilltoborder($im_temp,0,0,$bg,$bg); 
		}
		if ($corners & self::ROUND_TOP_RIGHT) {
			imagearc($im_temp, $startx-$startsize, $startsize,$arcsize, $arcsize, 270,360,$bg);
			imagefilltoborder($im_temp,$startx,0,$bg,$bg); 
		}
		if ($corners & self::ROUND_BOTTOM_LEFT) {
			imagearc($im_temp, $startsize, $starty-$startsize,$arcsize, $arcsize, 90,180,$bg);
			imagefilltoborder($im_temp,0,$starty,$bg,$bg); 
		}
		if ($corners & self::ROUND_BOTTOM_RIGHT) {
			imagearc($im_temp, $startx-$startsize, $starty-$startsize,$arcsize, $arcsize, 0,90,$bg);
			imagefilltoborder($im_temp,$startx,$starty,$bg,$bg); 
		}
		
		imagecopyresampled($this->thumb, $im_temp, 0, 0, 0, 0, $this->thumb_width,$this->thumb_height,$startx, $starty); 
		imagecolordeallocate($im_temp, $bg);
		
		// sharpening the thumb if possible
		if (function_exists('imageconvolution'))
		{
			$spnMatrix=array(array(-1,-1,-1,),array(-1,16,-1,),array(-1,-1,-1));
			$divisor=8;
			$offset=0;
			imageconvolution($this->thumb, $spnMatrix, $divisor, $offset);
		}
		
		return $this;
	}
	
	/**
	 * Put your photo watermark on the thumbnail.
	 *
	 * @param string $photo Path to your logo
	 * @param int $left Text pixel position from the left
	 * @param int $bottom Text pixel position from the bottom
	 * @param int $alpha Logo alpha if needed. Ranges between 0 and 100
	 * @throws ThumbnailerException
	 * @since 1.2.4
	 * @return Thumbnailer Self-reference for chaining
	 */
	public function &logoPhoto($photo, $left=10, $bottom=20, $alpha=0)
	{
		if (empty($this->thumb))
		{
			$this->thumb=$this->imagecreate($this->name);
			$this->thumb_width=imagesx($this->thumb);
			$this->thumb_height=imagesy($this->thumb);
		}
				
		$logo=new Thumbnailer($photo);
		$this->imagecopymergea($this->thumb, $logo->resource, $left, $this->thumb_height-$bottom, 0, 0, imagesx($logo->resource), imagesy($logo->resource), $alpha);
		unset($logo);

		
		return $this;
	}
	
	/**
	 * Put your text watermark on the thumbnail.
	 * Default position is bottom left.
	 *
	 * @param string $text Text to burn
	 * @param int $left Text pixel position from the left
	 * @param int $bottom Text pixel position from the bottom
	 * @throws ThumbnailerException
	 * @since 1.2.4
	 * @return Thumbnailer Self-reference for chaining
	 */
	public function &logoText($text, $left=10, $bottom=20, $color=array(255,255,255), $font=2)
	{
		if (empty($this->thumb))
		{
			$this->thumb=$this->imagecreate($this->name);
			$this->thumb_width=imagesx($this->thumb);
			$this->thumb_height=imagesy($this->thumb);
		}
		
		$color=imagecolorallocate($this->thumb, $color[0], $color[1], $color[2]);
		imagestring($this->thumb, $font, $left, $this->thumb_height-$bottom, $text, $color);
		imagecolordeallocate($this->thumb, $color);
		
		return $this;
	}
	
	/**
	 * Returns the color using RGB values;
	 *
	 * @param int $r Red value
	 * @param int $g Green value
	 * @param int $b Blue value
	 * @return array The color
	 * @since 1.1.1
	 */
	public static function colorRGB($r,$g,$b)
	{
		return array($r,$g,$b);
	}
	/**
	 * Returns the color using Hex value;
	 *
	 * @example Thumbnailer::colorHex('#FF0000')
	 * @param string $hex Hex value
	 * @return array The color
	 * @since 1.1.1
	 */
	
	public static function colorHex($hex)
	{
		return self::colorRGB(hexdec(substr($hex,1,2)),hexdec(substr($hex,3,2)),hexdec(substr($hex,5,2)));
	}
	
	/**
	 * Create a border over the thumb.
	 *
	 * @param array $color
	 * @return Thumbnailer Self-reference for chaining
	 * @since 1.2.1
	 * @throws ThumbnailerException
	 */
	public function border($color=array(0,0,0))
	{
		if (!is_resource($this->thumb)) {
			throw new ThumbnailerException('Cannot create border over empty thumb', 2);
		}
		
		$border=imagecolorallocate($this->thumb, $color[0],$color[1],$color[2]);
		imagerectangle($this->thumb, 0,0,$this->thumb_width-1,$this->thumb_height-1,$border);
		imagecolordeallocate($this->thumb, $border);
		return $this;
	}
	
	/**
	 * Turn thumb into grayscale.
	 *
	 * @return Thumbnailer Self-reference for chaining
	 * @throws ThumbnailerException
	 * @since 1.2.5
	 */
	public function &effectGray()
	{
		if (!is_resource($this->thumb)) {
			throw new ThumbnailerException('Cannot grayscale empty thumb', 8);
		}
		
		for ($x=0; $x<$this->thumb_width; $x++)
		{
			for ($y=0; $y<$this->thumb_height; $y++)
			{
				$rgb=imagecolorat($this->thumb, $x, $y); 
				$rr=($rgb >> 16) & 0xFF;
                $gg=($rgb >> 8) & 0xFF;
                $bb=$rgb & 0xFF;
                
                $gray=round(($rr + $gg + $bb) / 3);
                $color=imagecolorallocate($this->thumb, $gray, $gray, $gray);
                imagesetpixel($this->thumb, $x, $y, $color);
                imagecolordeallocate($this->thumb, $color);
			}
		}
		
		return $this;
	}
	
	/**
	 * Saves the thumb into given file.
	 * If no $file given, thumb will be flushed directly to browser.
	 * 
	 * @param string Where to save the thumb.
	 * @param int Thumb output quality. Default 85. (Only jpeg and png)
	 * @since 1.0.0
	 * @return mixed If thumb was generated returns its path otherwise false.
	 */
	public function save($file=null, $quality=85)
	{
		$f=$this->savefunc;
		
		// imagepng does not have 3rd parameter
		// workaround :)
		if ($f=='imagepng') {
			$quality=null;
		}
		
		if ($f($this->thumb, $file, $quality)!==false) {
			$path=realpath($file);
			
			// set filesize
			if ($file) {
				$this->filesize=filesize($path);
			}
			return $path;
		}
		
		return false;
	}
	
	/**
	 * Faster resample photo.
	 *
	 * @since 1.0.0
	 */
	protected function im(&$dim,$sim,$dx,$dy,$sx,$sy,$dw,$dh,$sw,$sh,$q=3)
	{
		if ((($dw*$q)<$sw||($dh*$q)<$sh)&&$q<5){
			$t=imagecreatetruecolor($dw*$q+1,$dh*$q+1);
			@imagecopyresized($t,$sim,0,0,$sx,$sy,$dw*$q+1,$dh*$q+1,$sw,$sh);
			@imagecopyresampled($dim,$t,$dx,$dy,0,0,$dw,$dh,$dw*$q,$dh*$q);
			@imagedestroy($t);
		}else @imagecopyresampled ($dim,$sim,$dx,$dy,$sx,$sy,$dw,$dh,$sw,$sh);
	}
	
	/**
	 * Create the image from the given photo.
	 *
	 * @param string $filename Photo filename
	 * @return resource gd image
	 * @since 1.2.0
	 * @throws ThumbnailerException
	 */
	protected function imagecreate($filename)
	{
		$photo=getimagesize($filename);
		$mime=image_type_to_mime_type($photo[2]);

		if (!empty($this->allowed))
		{
			if (!in_array($mime, $this->allowed))
				throw new ThumbnailerException($mime.' type is not supported',3);
		}
			
		switch($photo[2])
		{
			case IMAGETYPE_JPEG	: {
				$f='imagecreatefromjpeg';
				$this->savefunc='imagejpeg';
				break;
			}
			case IMAGETYPE_GIF	: {
				$f='imagecreatefromgif';
				$this->savefunc='imagegif';
				break;
			}
			case IMAGETYPE_PNG	: {
				$f='imagecreatefrompng';
				$this->savefunc='imagepng';
				break;
			}
			default: $f=false;
		}
		
		if (!$f)
			throw new ThumbnailerException('Unknown image type create function',4);
			
		$image=$f($filename);
		if (!$image)
			throw new ThumbnailerException('Could not create the image',5);
		
		return $image;
	}
	
	/**
	 * Create temporary image.
	 *
	 * @param int $width Width of the image
	 * @param int $height Height of the image
	 * @return resource Temporary image
	 * @since 1.2.0
	 */
	protected function createTmpImage($width, $height)
	{
		return $this->tmp[]=imagecreatetruecolor($width, $height);
	}
	
	/**
	 * Imagecopymerge with alpha channel handling.
	 * @since 1.2.4
	 */
	protected function imagecopymergea($dst_im, $src_im, $dst_x, $dst_y, $src_x, $src_y, $src_w, $src_h, $pct)
	{
		$w=imagesx($src_im);
		$h=imagesy($src_im);
        
        $cut=$this->createTmpImage($src_w, $src_h);
        imagecopy($cut, $dst_im, 0, 0, $dst_x, $dst_y, $src_w, $src_h);
        imagecopy($cut, $src_im, 0, 0, $src_x, $src_y, $src_w, $src_h);
        imagecopymerge($dst_im, $cut, $dst_x, $dst_y, $src_x, $src_y, $src_w, $src_h, 100-$pct);
    } 
	
	/**
	 * Sets default save location.
	 *
	 * @param string $location Where to save the thumbs.
	 */
	public static function setSaveLocation($location)
	{
		self::$saveLocation=$location;
	}
	
	/**
	 * Sets the filename under the thumb will be saved.
	 *
	 * @param string $name Thumb's filename
	 * @return Thumbnailer Self-reference for chaining
	 */
	public function setThumbName($name)
	{
		$this->thumbName=$name;
		return $this;
	}
	
	/**
	 * Set another prefix.
	 *
	 * @param string $prefix
	 */
	public static function setPrefix($prefix)
	{
		self::$prefix=$prefix;
	}
	
	/**
	 * Batch mode helper.
	 * For more information about the input mask
	 * please refer to php function's glob manual at
	 * http://php.net/glob
	 *
	 * @param string $in Mask to read from.
	 * @param string $out Output directory for thumbs
	 * @param function $callback Callback function
	 * @throws ThumbnailerException
	 * @return array User output
	 * @since 1.2.1
	 */
	public static function &batch($callback, $in, $out=false)
	{
		if (!function_exists($callback)) {
			throw new ThumbnailerException($callback.' function is not defined', 6);
		}
		
		if ($out) {
			Thumbnailer::setSaveLocation($out);
		}
		
		$return=array();
		foreach(glob($in,GLOB_BRACE) as $img)
		{
			$th=new Thumbnailer($img);
			$return[]=$callback($th);
			unset($th);
		}
		
		return $return;
	}
	
	/**
	 * Get original filename.
	 *
	 * @return string Photo filename
	 */
	public function getFilename()
	{
		return basename($this->name);
	}
	
	/**
	 * Set allowed mime types.
	 *
	 * @param array $allowed
	 */
	public static function setAllowed($allowed)
	{
		self::$allowed=$allowed;
	}
	
	/**
	 * Get thumb's filesize.
	 * 
	 * @return int
	 * @since 1.2.5
	 */
	public function getFilesize()
	{
		return $this->filesize;
	}
	
}

?>