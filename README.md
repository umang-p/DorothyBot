# dorothybot - Multipurpose bot for Discord

dorothybot is a simple discord bot designed for single shard use. Its current and planned features are designed purely on the needs of its current users. The code has primarily been made public as a means for others to learn from or provide similar functionalities in other applications.

### Usage
The project can be compiled and built as is, with the addition of your bot's token in `dorothy.java`. Dependencies include [LavaPlayer](https://github.com/sedmelluq/lavaplayer), [Discord4J](https://github.com/Discord4J/Discord4J), and [jsoup](https://jsoup.org/). See `pom.xml` for additional details.

### Commands

#### audio - Commands for playing audio from various [sources](https://github.com/sedmelluq/lavaplayer#supported-formats)

`nowplaying` `np` : Show title of the currently playing track

`pause` : Pause audio

`resume` : Resume playing audio

`volume` : Change volume

`skip` : Skip the currently playing track

`playlist` `pl` `queue` : Show the tracks that are in the queue

`remove` `rm`: Remove a specific track from the queue

`stop` : Clear queue, stop playing audio, and have bot leave voice channel

`play` : Start playing a track or add it to the queue if something is playing already. If the link provided is a playlist, only uses the selected track

`playall` : Same as `play` but if given a playlist link, all tracks in the playlist will be added to the queue



#### gf - Commands related to the Girls' Frontline mobile game (All data is sourced from GFDB)

`dolls` : Show which Tactical-dolls can be obtained from a given recipe or timer

`equips` : Show which equipment can be obtained from a given recipe or timer

`recipes` : Show which recipes can be used to obtain a specific Tactical-doll


#### jp - Commands related to learning Japanese

`jisho` : Searches [Jisho.org](https://jisho.org/) for kanji, kana, or even english.

#### misc - Miscellaneous commands

`logout` `shutdown`: Shuts down the bot. Can only be used by the bot owner.

`help` : Shows help text

`fetchgfdata` : Updates the JSON files used for gf commands. Can only be used by the bot owner.