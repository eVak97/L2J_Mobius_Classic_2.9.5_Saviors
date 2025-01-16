/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.l2jmobius.gameserver.network.serverpackets;

import java.util.List;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.instancemanager.FortSiegeManager;
import org.l2jmobius.gameserver.model.FortSiegeSpawn;
import org.l2jmobius.gameserver.model.Spawn;
import org.l2jmobius.gameserver.model.siege.Fort;
import org.l2jmobius.gameserver.model.siege.FortSiege;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;

/**
 * @author KenM
 */
public class ExShowFortressMapInfo extends ServerPacket
{
	private final Fort _fortress;
	private final FortSiege _siege;
	private final List<FortSiegeSpawn> _commanders;
	
	public ExShowFortressMapInfo(Fort fortress)
	{
		_fortress = fortress;
		_siege = fortress.getSiege();
		_commanders = FortSiegeManager.getInstance().getCommanderSpawnList(fortress.getResidenceId());
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_SHOW_FORTRESS_MAP_INFO.writeId(this, buffer);
		buffer.writeInt(_fortress.getResidenceId());
		buffer.writeInt(_siege.isInProgress()); // fortress siege status
		buffer.writeInt(_fortress.getFortSize()); // barracks count
		if ((_commanders != null) && !_commanders.isEmpty() && _siege.isInProgress())
		{
			switch (_commanders.size())
			{
				case 3:
				{
					for (FortSiegeSpawn spawn : _commanders)
					{
						if (isSpawned(spawn.getId()))
						{
							buffer.writeInt(0);
						}
						else
						{
							buffer.writeInt(1);
						}
					}
					break;
				}
				case 4: // TODO: change 4 to 5 once control room supported
				{
					int count = 0;
					for (FortSiegeSpawn spawn : _commanders)
					{
						count++;
						if (count == 4)
						{
							buffer.writeInt(1); // TODO: control room emulated
						}
						if (isSpawned(spawn.getId()))
						{
							buffer.writeInt(0);
						}
						else
						{
							buffer.writeInt(1);
						}
					}
					break;
				}
			}
		}
		else
		{
			for (int i = 0; i < _fortress.getFortSize(); i++)
			{
				buffer.writeInt(0);
			}
		}
	}
	
	private boolean isSpawned(int npcId)
	{
		boolean ret = false;
		for (Spawn spawn : _siege.getCommanders())
		{
			if (spawn.getId() == npcId)
			{
				ret = true;
				break;
			}
		}
		return ret;
	}
}
