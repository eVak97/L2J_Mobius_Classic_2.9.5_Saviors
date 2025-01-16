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

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;

/**
 * @author VISTALL
 */
public class ExSubPledgeSkillAdd extends ServerPacket
{
	private final int _type;
	private final int _skillId;
	private final int _skillLevel;
	
	public ExSubPledgeSkillAdd(int type, int skillId, int skillLevel)
	{
		_type = type;
		_skillId = skillId;
		_skillLevel = skillLevel;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_SUB_PLEDGET_SKILL_ADD.writeId(this, buffer);
		buffer.writeInt(_type);
		buffer.writeInt(_skillId);
		buffer.writeInt(_skillLevel);
	}
}
