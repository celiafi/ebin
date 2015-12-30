# ebin
E-Book Investigator for DTBook validation and analysis

## Introduction

ebin is an audio analysis tool tailored specifically for analysis of DTBooks. ebin analyses batches of files for their digital peak level, true peak (ISP) level, signal-to-noise ratio and LUFS.

## Dependencies

ebin uses [ffmpeg](https://www.ffmpeg.org/) and [sox](http://sox.sourceforge.net/), so grab those. ebin by default looks for sox.exe and ffmpeg.exe in the directory ebin itself resides, so move / copy them there. Furthermore, depending on the input files, ebin may require that sox has `libmad.dll` (which it should by default).

## Installation and usage

Download `ebin.jar` (from `dist` folder) and obtain the required dependencies. Run ebin from CLI with `java -jar ebin.jar dir report`, where `dir` is the directory containing audio files to be analysed and `report` is the report location.

There are no configuration options.

## Known bugs

No known bugs.

## Building and hacking

Standard Java build. Parsing constants should *not* be changed. If tolerances for acceptance change, static variables in Ebin.java have to be changed to reflect new values.

## License

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.