// port-lint: source dev/src/ref_raster.rs
package io.github.kotlinmania.abglyph


/**
 * Draw a 16px 'w' character from DejaVuSansMono.ttf.
 */
fun rasterizeTtfW(): Rasterizer {
    val r = Rasterizer.new(9, 8);
    r.drawLine(point(0.0f, 0.48322153f), point(1.2214766f, 0.48322153f));
    r.drawLine(point(1.2214766f, 0.48322153f), point(2.5302014f, 6.557047f));
    r.drawLine(point(2.5302014f, 6.557047f), point(3.6040268f, 2.6778522f));
    r.drawLine(point(3.6040268f, 2.6778522f), point(4.657718f, 2.6778522f));
    r.drawLine(point(4.657718f, 2.6778522f), point(5.7449665f, 6.557047f));
    r.drawLine(point(5.7449665f, 6.557047f), point(7.0536914f, 0.48322153f));
    r.drawLine(point(7.0536914f, 0.48322153f), point(8.275167f, 0.48322153f));
    r.drawLine(point(8.275167f, 0.48322153f), point(6.5167785f, 8.0f));
    r.drawLine(point(6.5167785f, 8.0f), point(5.3355703f, 8.0f));
    r.drawLine(point(5.3355703f, 8.0f), point(4.134228f, 3.8791947f));
    r.drawLine(point(4.134228f, 3.8791947f), point(2.9395974f, 8.0f));
    r.drawLine(point(2.9395974f, 8.0f), point(1.7583892f, 8.0f));
    r.drawLine(point(1.7583892f, 8.0f), point(0.0f, 0.48322153f));
    r.drawLine(point(0.0f, 0.48322153f), point(0.0f, 0.48322153f));
    return r
}

/**
 * Draw a 60px 'ΐ' character from OpenSans-Italic.ttf.
 */
fun rasterizeTtfIota(): Rasterizer {
    val r = Rasterizer.new(14, 38);
    r.drawQuad(point(6.1964865f, 34.482967f), point(7.2291145f, 34.482967f), point(9.186806f, 33.92363f));
    r.drawLine(point(9.186806f, 33.92363f), point(9.186806f, 36.69882f));
    r.drawQuad(point(9.186806f, 36.69882f), point(8.5199f, 36.978485f), point(7.455002f, 37.204376f));
    r.drawQuad(point(7.455002f, 37.204376f), point(6.3901043f, 37.430264f), point(5.572607f, 37.430264f));
    r.drawQuad(point(5.572607f, 37.430264f), point(2.9695234f, 37.430264f), point(1.6034422f, 36.150234f));
    r.drawQuad(point(1.6034422f, 36.150234f), point(0.2373612f, 34.870205f), point(0.2373612f, 32.33166f));
    r.drawQuad(point(0.2373612f, 32.33166f), point(0.2373612f, 31.621727f), point(0.4417355f, 30.470778f));
    r.drawQuad(point(0.4417355f, 30.470778f), point(0.6461098f, 29.319828f), point(4.0021515f, 13.421656f));
    r.drawLine(point(4.0021515f, 13.421656f), point(7.594837f, 13.421656f));
    r.drawLine(point(7.594837f, 13.421656f), point(4.2603087f, 29.25529f));
    r.drawQuad(point(4.2603087f, 29.25529f), point(3.894586f, 30.890284f), point(3.894586f, 31.92291f));
    r.drawQuad(point(3.894586f, 31.92291f), point(3.894586f, 34.482967f), point(6.1964865f, 34.482967f));
    r.drawLine(point(6.1964865f, 34.482967f), point(6.1964865f, 34.482967f));
    r.drawQuad(point(5.615633f, 6.645033f), point(7.89602f, 2.6220856f), point(8.993188f, 0.08354187f));
    r.drawLine(point(8.993188f, 0.08354187f), point(13.18824f, 0.08354187f));
    r.drawLine(point(13.18824f, 0.08354187f), point(13.18824f, 0.5138016f));
    r.drawQuad(point(13.18824f, 0.5138016f), point(12.112585f, 1.9982071f), point(10.456078f, 3.837574f));
    r.drawQuad(point(10.456078f, 3.837574f), point(8.79957f, 5.6769447f), point(7.2291145f, 7.1183205f));
    r.drawLine(point(7.2291145f, 7.1183205f), point(5.615633f, 7.1183205f));
    r.drawLine(point(5.615633f, 7.1183205f), point(5.615633f, 6.645033f));
    r.drawLine(point(5.615633f, 6.645033f), point(5.615633f, 6.645033f));
    r.drawQuad(point(0.94729304f, 7.354965f), point(0.94729304f, 6.300823f), point(1.560416f, 5.612406f));
    r.drawQuad(point(1.560416f, 5.612406f), point(2.1735392f, 4.9239845f), point(3.1416278f, 4.9239845f));
    r.drawQuad(point(3.1416278f, 4.9239845f), point(4.8196487f, 4.9239845f), point(4.8196487f, 6.645033f));
    r.drawQuad(point(4.8196487f, 6.645033f), point(4.8196487f, 7.720688f), point(4.1635f, 8.441376f));
    r.drawQuad(point(4.1635f, 8.441376f), point(3.5073504f, 9.162064f), point(2.6898532f, 9.162064f));
    r.drawQuad(point(2.6898532f, 9.162064f), point(1.9368951f, 9.162064f), point(1.4420941f, 8.688776f));
    r.drawQuad(point(1.4420941f, 8.688776f), point(0.94729304f, 8.215488f), point(0.94729304f, 7.354965f));
    r.drawLine(point(0.94729304f, 7.354965f), point(0.94729304f, 7.354965f));
    r.drawQuad(point(9.617067f, 7.354965f), point(9.617067f, 6.365364f), point(10.219434f, 5.6446743f));
    r.drawQuad(point(10.219434f, 5.6446743f), point(10.8218f, 4.9239845f), point(11.811402f, 4.9239845f));
    r.drawQuad(point(11.811402f, 4.9239845f), point(13.489423f, 4.9239845f), point(13.489423f, 6.645033f));
    r.drawQuad(point(13.489423f, 6.645033f), point(13.489423f, 7.699175f), point(12.854787f, 8.43062f));
    r.drawQuad(point(12.854787f, 8.43062f), point(12.220151f, 9.162064f), point(11.381141f, 9.162064f));
    r.drawQuad(point(11.381141f, 9.162064f), point(10.628182f, 9.162064f), point(10.122625f, 8.688776f));
    r.drawQuad(point(10.122625f, 8.688776f), point(9.617067f, 8.215488f), point(9.617067f, 7.354965f));
    r.drawLine(point(9.617067f, 7.354965f), point(9.617067f, 7.354965f));
    return r
}

/**
 * Draw a 600px U+2623 character from DejaVuSansMono.ttf.
 */
fun rasterizeTtfBiohazard(): Rasterizer {
    val r = Rasterizer.new(294, 269);
    r.drawQuad(point(176.22818f, 196.51007f), point(188.30872f, 189.21141f), point(195.22986f, 175.74664f));
    r.drawQuad(point(195.22986f, 175.74664f), point(202.151f, 162.28189f), point(200.89261f, 148.4396f));
    r.drawQuad(point(200.89261f, 148.4396f), point(207.43623f, 145.92282f), point(213.97986f, 144.9161f));
    r.drawQuad(point(213.97986f, 144.9161f), point(216.24495f, 165.5537f), point(204.41609f, 184.55537f));
    r.drawQuad(point(204.41609f, 184.55537f), point(192.58723f, 203.55705f), point(177.48657f, 210.10068f));
    r.drawQuad(point(177.48657f, 210.10068f), point(174.46643f, 203.30537f), point(176.22818f, 196.51007f));
    r.drawLine(point(176.22818f, 196.51007f), point(176.22818f, 196.51007f));
    r.drawQuad(point(173.20804f, 104.89934f), point(160.87582f, 98.104034f), point(145.77516f, 98.73323f));
    r.drawQuad(point(145.77516f, 98.73323f), point(130.67448f, 99.36243f), point(119.60066f, 107.164444f));
    r.drawQuad(point(119.60066f, 107.164444f), point(113.81207f, 103.13759f), point(110.03691f, 98.104034f));
    r.drawQuad(point(110.03691f, 98.104034f), point(126.647644f, 85.52014f), point(148.92113f, 86.40102f));
    r.drawQuad(point(148.92113f, 86.40102f), point(171.19463f, 87.28189f), point(184.53355f, 97.09732f));
    r.drawQuad(point(184.53355f, 97.09732f), point(180.00334f, 102.88591f), point(173.20804f, 104.89934f));
    r.drawLine(point(173.20804f, 104.89934f), point(173.20804f, 104.89934f));
    r.drawQuad(point(95.69127f, 152.71812f), point(95.69127f, 166.56041f), point(103.744965f, 179.39598f));
    r.drawQuad(point(103.744965f, 179.39598f), point(111.79865f, 192.23154f), point(124.38254f, 198.02014f));
    r.drawQuad(point(124.38254f, 198.02014f), point(123.879196f, 204.81543f), point(121.11073f, 210.85571f));
    r.drawQuad(point(121.11073f, 210.85571f), point(102.48657f, 202.55034f), point(91.66443f, 182.79362f));
    r.drawQuad(point(91.66443f, 182.79362f), point(80.84228f, 163.03693f), point(82.8557f, 146.67786f));
    r.drawQuad(point(82.8557f, 146.67786f), point(90.40604f, 147.68457f), point(95.69127f, 152.71812f));
    r.drawLine(point(95.69127f, 152.71812f), point(95.69127f, 152.71812f));
    r.drawQuad(point(262.302f, 253.38927f), point(242.9228f, 268.23825f), point(213.22482f, 268.23825f));
    r.drawQuad(point(213.22482f, 268.23825f), point(204.91945f, 268.23825f), point(196.11073f, 266.97986f));
    r.drawQuad(point(196.11073f, 266.97986f), point(166.66443f, 260.68793f), point(150.30536f, 230.2349f));
    r.drawLine(point(150.30536f, 230.2349f), point(150.05368f, 230.48657f));
    r.drawQuad(point(150.05368f, 230.48657f), point(139.23154f, 246.34229f), point(117.83892f, 258.6745f));
    r.drawQuad(point(117.83892f, 258.6745f), point(101.22818f, 266.97986f), point(84.11409f, 266.97986f));
    r.drawQuad(point(84.11409f, 266.97986f), point(61.96644f, 266.97986f), point(39.063755f, 253.64095f));
    r.drawQuad(point(39.063755f, 253.64095f), point(64.73489f, 265.72147f), point(88.0151f, 257.4161f));
    r.drawQuad(point(88.0151f, 257.4161f), point(111.295296f, 249.11073f), point(121.86577f, 231.74496f));
    r.drawQuad(point(121.86577f, 231.74496f), point(136.71475f, 202.55034f), point(125.38925f, 169.32886f));
    r.drawLine(point(125.38925f, 169.32886f), point(137.72147f, 161.77853f));
    r.drawQuad(point(137.72147f, 161.77853f), point(149.802f, 170.33557f), point(160.1208f, 162.03021f));
    r.drawLine(point(160.1208f, 162.03021f), point(171.69798f, 169.07718f));
    r.drawQuad(point(171.69798f, 169.07718f), point(160.1208f, 199.78189f), point(176.98322f, 226.71141f));
    r.drawQuad(point(176.98322f, 226.71141f), point(188.05704f, 247.09732f), point(207.81375f, 254.52182f));
    r.drawQuad(point(207.81375f, 254.52182f), point(227.57047f, 261.94632f), point(262.302f, 253.38927f));
    r.drawLine(point(262.302f, 253.38927f), point(262.302f, 253.38927f));
    r.drawQuad(point(181.51341f, 0.45303345f), point(210.45636f, 12.533569f), point(227.06711f, 51.29196f));
    r.drawQuad(point(227.06711f, 51.29196f), point(235.87582f, 79.73155f), point(217.75502f, 109.17786f));
    r.drawLine(point(217.75502f, 109.17786f), point(218.25838f, 109.17786f));
    r.drawQuad(point(218.25838f, 109.17786f), point(237.13422f, 110.939606f), point(258.52682f, 123.02014f));
    r.drawQuad(point(258.52682f, 123.02014f), point(293.51004f, 146.1745f), point(293.51004f, 192.23154f));
    r.drawLine(point(293.51004f, 192.23154f), point(293.51004f, 193.99329f));
    r.drawQuad(point(293.51004f, 193.99329f), point(291.24496f, 166.05705f), point(272.49496f, 149.698f));
    r.drawQuad(point(272.49496f, 149.698f), point(253.74496f, 133.33893f), point(233.61073f, 133.33893f));
    r.drawQuad(point(233.61073f, 133.33893f), point(200.89261f, 135.10068f), point(177.48657f, 161.52686f));
    r.drawLine(point(177.48657f, 161.52686f), point(165.15436f, 154.2282f));
    r.drawQuad(point(165.15436f, 154.2282f), point(166.41275f, 139.63087f), point(153.82886f, 134.849f));
    r.drawLine(point(153.82886f, 134.849f), point(153.82886f, 121.51007f));
    r.drawQuad(point(153.82886f, 121.51007f), point(186.29529f, 115.97316f), point(201.39597f, 88.03693f));
    r.drawQuad(point(201.39597f, 88.03693f), point(213.4765f, 67.902695f), point(210.07884f, 47.139267f));
    r.drawQuad(point(210.07884f, 47.139267f), point(206.6812f, 26.375854f), point(181.51341f, 0.45303345f));
    r.drawLine(point(181.51341f, 0.45303345f), point(181.51341f, 0.45303345f));
    r.drawQuad(point(1.5637579f, 197.01343f), point(0.8087244f, 192.7349f), point(0.8087244f, 188.45639f));
    r.drawQuad(point(0.8087244f, 188.45639f), point(0.8087244f, 161.77853f), point(22.453018f, 132.83557f));
    r.drawQuad(point(22.453018f, 132.83557f), point(42.838924f, 110.68793f), point(77.31879f, 111.69464f));
    r.drawLine(point(77.31879f, 111.69464f), point(77.06711f, 111.44296f));
    r.drawQuad(point(77.06711f, 111.44296f), point(69.2651f, 93.825516f), point(68.76174f, 69.412766f));
    r.drawQuad(point(68.76174f, 69.412766f), point(71.781876f, 21.090622f), point(121.11073f, 0.9563904f));
    r.drawQuad(point(121.11073f, 0.9563904f), point(89.651f, 19.580551f), point(84.994965f, 43.9933f));
    r.drawQuad(point(84.994965f, 43.9933f), point(80.33892f, 68.40605f), point(90.15436f, 86.27518f));
    r.drawQuad(point(90.15436f, 86.27518f), point(108.27516f, 113.45639f), point(142.50334f, 120.25168f));
    r.drawLine(point(142.50334f, 120.25168f), point(142.75502f, 135.10068f));
    r.drawQuad(point(142.75502f, 135.10068f), point(129.16443f, 141.14095f), point(131.17784f, 154.2282f));
    r.drawLine(point(131.17784f, 154.2282f), point(119.85234f, 160.77182f));
    r.drawQuad(point(119.85234f, 160.77182f), point(98.7114f, 135.35236f), point(67.25167f, 136.35907f));
    r.drawQuad(point(67.25167f, 136.35907f), point(43.593956f, 136.10739f), point(27.486576f, 149.44632f));
    r.drawQuad(point(27.486576f, 149.44632f), point(11.379194f, 162.78525f), point(1.5637579f, 197.01343f));
    r.drawLine(point(1.5637579f, 197.01343f), point(1.5637579f, 197.01343f));
    return r
}

/**
 * Draw a 300px 'ę' character from Exo2-Light.otf.
 */
fun rasterizeOtfTailedE(): Rasterizer {
    val r = Rasterizer.new(106, 183);
    r.drawCubic(point(55.25f, 0.75f), point(16.75f, 0.75f), point(0.75f, 17.0f), point(0.75f, 64.0f));
    r.drawCubic(point(0.75f, 64.0f), point(0.75f, 110.5f), point(16.75f, 128.0f), point(57.5f, 128.0f));
    r.drawCubic(point(57.5f, 128.0f), point(66.75f, 128.0f), point(80.0f, 127.0f), point(90.75f, 125.25f));
    r.drawCubic(point(90.75f, 125.25f), point(79.75f, 135.25f), point(74.0f, 144.25f), point(71.5f, 151.75f));
    r.drawCubic(point(71.5f, 151.75f), point(65.25f, 169.0f), point(76.5f, 183.0f), point(105.75f, 170.25f));
    r.drawLine(point(105.75f, 170.25f), point(104.0f, 163.5f));
    r.drawCubic(point(104.0f, 163.5f), point(87.0f, 169.25f), point(78.0f, 164.75f), point(83.25f, 151.5f));
    r.drawCubic(point(83.25f, 151.5f), point(87.5f, 140.0f), point(94.5f, 130.75f), point(102.5f, 122.25f));
    r.drawLine(point(102.5f, 122.25f), point(103.0f, 122.0f));
    r.drawLine(point(103.0f, 122.0f), point(101.25f, 111.25f));
    r.drawCubic(point(101.25f, 111.25f), point(89.75f, 113.0f), point(73.5f, 114.25f), point(59.5f, 114.25f));
    r.drawCubic(point(59.5f, 114.25f), point(31.75f, 114.25f), point(19.5f, 105.5f), point(17.75f, 72.25f));
    r.drawLine(point(17.75f, 72.25f), point(77.75f, 72.25f));
    r.drawCubic(point(77.75f, 72.25f), point(97.75f, 72.25f), point(106.0f, 60.25f), point(105.75f, 38.5f));
    r.drawCubic(point(105.75f, 38.5f), point(105.5f, 13.5f), point(90.0f, 0.75f), point(55.25f, 0.75f));
    r.drawLine(point(55.25f, 0.75f), point(55.25f, 0.75f));
    r.drawCubic(point(56.0f, 14.5f), point(79.5f, 14.5f), point(89.5f, 21.75f), point(89.75f, 38.75f));
    r.drawCubic(point(89.75f, 38.75f), point(90.0f, 50.5f), point(86.75f, 59.75f), point(74.25f, 59.75f));
    r.drawLine(point(74.25f, 59.75f), point(17.5f, 59.75f));
    r.drawCubic(point(17.5f, 59.75f), point(18.0f, 25.5f), point(28.0f, 14.5f), point(56.0f, 14.5f));
    r.drawLine(point(56.0f, 14.5f), point(56.0f, 14.5f));
    return r
}

/**
 * Draw a 300px 'ę' character from Exo2-Light.ttf.
 */
fun rasterizeTtfTailedE(): Rasterizer {
    val r = Rasterizer.new(106, 177);
    r.drawQuad(point(55.25f, 0.75f), point(81.25f, 0.75f), point(93.375f, 10.25f));
    r.drawQuad(point(93.375f, 10.25f), point(105.5f, 19.75f), point(105.75f, 38.5f));
    r.drawQuad(point(105.75f, 38.5f), point(106.0f, 54.75f), point(99.375f, 63.5f));
    r.drawQuad(point(99.375f, 63.5f), point(92.75f, 72.25f), point(77.75f, 72.25f));
    r.drawLine(point(77.75f, 72.25f), point(17.75f, 72.25f));
    r.drawQuad(point(17.75f, 72.25f), point(18.5f, 88.75f), point(23.125f, 97.875f));
    r.drawQuad(point(23.125f, 97.875f), point(27.75f, 107.0f), point(36.75f, 110.625f));
    r.drawQuad(point(36.75f, 110.625f), point(45.75f, 114.25f), point(59.5f, 114.25f));
    r.drawQuad(point(59.5f, 114.25f), point(70.0f, 114.25f), point(81.375f, 113.375f));
    r.drawQuad(point(81.375f, 113.375f), point(92.75f, 112.5f), point(101.25f, 111.25f));
    r.drawLine(point(101.25f, 111.25f), point(103.0f, 122.0f));
    r.drawQuad(point(103.0f, 122.0f), point(97.75f, 124.0f), point(89.625f, 125.375f));
    r.drawQuad(point(89.625f, 125.375f), point(81.5f, 126.75f), point(72.875f, 127.375f));
    r.drawQuad(point(72.875f, 127.375f), point(64.25f, 128.0f), point(57.5f, 128.0f));
    r.drawQuad(point(57.5f, 128.0f), point(37.0f, 128.0f), point(24.625f, 121.625f));
    r.drawQuad(point(24.625f, 121.625f), point(12.25f, 115.25f), point(6.5f, 101.25f));
    r.drawQuad(point(6.5f, 101.25f), point(0.75f, 87.25f), point(0.75f, 64.0f));
    r.drawQuad(point(0.75f, 64.0f), point(0.75f, 40.5f), point(6.375f, 26.625f));
    r.drawQuad(point(6.375f, 26.625f), point(12.0f, 12.75f), point(24.0f, 6.75f));
    r.drawQuad(point(24.0f, 6.75f), point(36.0f, 0.75f), point(55.25f, 0.75f));
    r.drawLine(point(55.25f, 0.75f), point(55.25f, 0.75f));
    r.drawQuad(point(56.0f, 14.5f), point(42.0f, 14.5f), point(33.625f, 18.625f));
    r.drawQuad(point(33.625f, 18.625f), point(25.25f, 22.75f), point(21.5f, 32.625f));
    r.drawQuad(point(21.5f, 32.625f), point(17.75f, 42.5f), point(17.5f, 59.75f));
    r.drawLine(point(17.5f, 59.75f), point(74.25f, 59.75f));
    r.drawQuad(point(74.25f, 59.75f), point(83.5f, 59.75f), point(86.75f, 53.75f));
    r.drawQuad(point(86.75f, 53.75f), point(90.0f, 47.75f), point(89.75f, 38.75f));
    r.drawQuad(point(89.75f, 38.75f), point(89.5f, 26.0f), point(81.5f, 20.25f));
    r.drawQuad(point(81.5f, 20.25f), point(73.5f, 14.5f), point(56.0f, 14.5f));
    r.drawLine(point(56.0f, 14.5f), point(56.0f, 14.5f));
    r.drawLine(point(95.25f, 121.5f), point(103.0f, 122.0f));
    r.drawQuad(point(103.0f, 122.0f), point(97.0f, 128.5f), point(91.75f, 135.625f));
    r.drawQuad(point(91.75f, 135.625f), point(86.5f, 142.75f), point(83.25f, 151.5f));
    r.drawQuad(point(83.25f, 151.5f), point(79.25f, 161.5f), point(85.375f, 164.625f));
    r.drawQuad(point(85.375f, 164.625f), point(91.5f, 167.75f), point(104.0f, 163.75f));
    r.drawLine(point(104.0f, 163.75f), point(106.0f, 170.25f));
    r.drawQuad(point(106.0f, 170.25f), point(91.25f, 176.75f), point(82.625f, 175.125f));
    r.drawQuad(point(82.625f, 175.125f), point(74.0f, 173.5f), point(71.25f, 167.0f));
    r.drawQuad(point(71.25f, 167.0f), point(68.5f, 160.5f), point(71.5f, 151.75f));
    r.drawQuad(point(71.5f, 151.75f), point(73.75f, 145.5f), point(79.25f, 137.875f));
    r.drawQuad(point(79.25f, 137.875f), point(84.75f, 130.25f), point(95.25f, 121.5f));
    r.drawLine(point(95.25f, 121.5f), point(95.25f, 121.5f));
    return r
}
