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
package quests.Q00380_BringOutTheFlavorOfIngredients;

import org.l2jmobius.gameserver.enums.QuestSound;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.quest.State;

public class Q00380_BringOutTheFlavorOfIngredients extends Quest
{
	// Monsters
	private static final int DIRE_WOLF = 20205;
	private static final int KADIF_WEREWOLF = 20206;
	private static final int GIANT_MIST_LEECH = 20225;
	// Items
	private static final int RITRON_FRUIT = 5895;
	private static final int MOON_FACE_FLOWER = 5896;
	private static final int LEECH_FLUIDS = 5897;
	private static final int ANTIDOTE = 1831;
	// Rewards
	private static final int RITRON_JELLY = 5960;
	private static final int JELLY_RECIPE = 5959;
	
	public Q00380_BringOutTheFlavorOfIngredients()
	{
		super(380);
		registerQuestItems(RITRON_FRUIT, MOON_FACE_FLOWER, LEECH_FLUIDS);
		addStartNpc(30069); // Rollant
		addTalkId(30069);
		addKillId(DIRE_WOLF, KADIF_WEREWOLF, GIANT_MIST_LEECH);
	}
	
	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		final QuestState st = getQuestState(player, false);
		if (st == null)
		{
			return htmltext;
		}
		
		if (event.equals("30069-04.htm"))
		{
			st.startQuest();
		}
		else if (event.equals("30069-12.htm"))
		{
			giveItems(player, JELLY_RECIPE, 1);
			st.exitQuest(true, true);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(Npc npc, Player player)
	{
		String htmltext = getNoQuestMsg(player);
		final QuestState st = getQuestState(player, true);
		
		switch (st.getState())
		{
			case State.CREATED:
			{
				htmltext = (player.getLevel() < 24) ? "30069-00.htm" : "30069-01.htm";
				break;
			}
			case State.STARTED:
			{
				final int cond = st.getCond();
				if (cond == 1)
				{
					htmltext = "30069-06.htm";
				}
				else if (cond == 2)
				{
					if (getQuestItemsCount(player, ANTIDOTE) >= 2)
					{
						htmltext = "30069-07.htm";
						st.setCond(3, true);
						takeItems(player, RITRON_FRUIT, -1);
						takeItems(player, MOON_FACE_FLOWER, -1);
						takeItems(player, LEECH_FLUIDS, -1);
						takeItems(player, ANTIDOTE, 2);
					}
					else
					{
						htmltext = "30069-06.htm";
					}
				}
				else if (cond == 3)
				{
					htmltext = "30069-08.htm";
					st.setCond(4, true);
				}
				else if (cond == 4)
				{
					htmltext = "30069-09.htm";
					st.setCond(5, true);
				}
				else if (cond == 5)
				{
					htmltext = "30069-10.htm";
					st.setCond(6, true);
				}
				else if (cond == 6)
				{
					giveItems(player, RITRON_JELLY, 1);
					if (getRandom(100) < 55)
					{
						htmltext = "30069-11.htm";
					}
					else
					{
						htmltext = "30069-13.htm";
						st.exitQuest(true, true);
					}
				}
				break;
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(Npc npc, Player player, boolean isPet)
	{
		final QuestState st = getQuestState(player, false);
		if ((st == null) || !st.isCond(1))
		{
			return null;
		}
		
		switch (npc.getId())
		{
			case DIRE_WOLF:
			{
				if ((getRandom(10) < 1) && (getQuestItemsCount(player, RITRON_FRUIT) < 4))
				{
					giveItems(player, RITRON_FRUIT, 1);
					if (getQuestItemsCount(player, RITRON_FRUIT) < 4)
					{
						playSound(player, QuestSound.ITEMSOUND_QUEST_ITEMGET);
					}
					else
					{
						if ((getQuestItemsCount(player, MOON_FACE_FLOWER) == 20) && (getQuestItemsCount(player, LEECH_FLUIDS) == 10))
						{
							st.setCond(2, true);
						}
						else
						{
							playSound(player, QuestSound.ITEMSOUND_QUEST_ITEMGET);
						}
					}
				}
				break;
			}
			case KADIF_WEREWOLF:
			{
				if ((getRandom(10) < 5) && (getQuestItemsCount(player, MOON_FACE_FLOWER) < 20))
				{
					giveItems(player, MOON_FACE_FLOWER, 1);
					if (getQuestItemsCount(player, MOON_FACE_FLOWER) < 20)
					{
						playSound(player, QuestSound.ITEMSOUND_QUEST_ITEMGET);
					}
					else
					{
						if ((getQuestItemsCount(player, RITRON_FRUIT) == 4) && (getQuestItemsCount(player, LEECH_FLUIDS) == 10))
						{
							st.setCond(2, true);
						}
						else
						{
							playSound(player, QuestSound.ITEMSOUND_QUEST_ITEMGET);
						}
					}
				}
				break;
			}
			case GIANT_MIST_LEECH:
			{
				if ((getRandom(10) < 5) && (getQuestItemsCount(player, LEECH_FLUIDS) < 10))
				{
					giveItems(player, LEECH_FLUIDS, 1);
					if (getQuestItemsCount(player, LEECH_FLUIDS) < 10)
					{
						playSound(player, QuestSound.ITEMSOUND_QUEST_ITEMGET);
					}
					else
					{
						if ((getQuestItemsCount(player, RITRON_FRUIT) == 4) && (getQuestItemsCount(player, MOON_FACE_FLOWER) == 20))
						{
							st.setCond(2, true);
						}
						else
						{
							playSound(player, QuestSound.ITEMSOUND_QUEST_ITEMGET);
						}
					}
				}
				break;
			}
		}
		
		return null;
	}
}
