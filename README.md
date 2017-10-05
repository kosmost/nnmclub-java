# NNMClub (java client)
Automated tool to monitor and download updated torrent from popular russian tracker nnm-club. Useful for series and frequently updating torrents.

How it works.
User can confugre list of monitored topics. Tool can be launched manually or with scheduler. When launched, tool fetches RSS feed from tracker and looks for topic specified in configured list. If any matches, tool dowloads torrent file attached to feed item. Properly configured torrent client will pick fresh file automatically and torrent download started.

Works with RSS feed. Steps to setup:
1. Specify target folder. This folder must be monitored by your torrent client.
2. Specify your personal passkey from tracker profile.
3. Fill list of monitored topics by ids
4. Schedule automatic execution in poll mode

Tool fetches RSS feed with torrents which was updated in last hour. So recommended period for scheduler is once per hour.
