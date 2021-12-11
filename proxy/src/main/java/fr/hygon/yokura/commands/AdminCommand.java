/*
 * Copyright (C) 2018 Velocity Contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package fr.hygon.yokura.commands;

import com.google.common.collect.ImmutableList;
import com.mongodb.BasicDBObject;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.proxy.VelocityServer;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bson.BsonDocument;
import org.bson.BsonString;

public class AdminCommand implements SimpleCommand {
  private final VelocityServer velocityServer;

  public AdminCommand(VelocityServer velocityServer) {
    this.velocityServer = velocityServer;
  }

  @Override
  public void execute(Invocation invocation) {
    if (invocation.arguments().length == 0) {
      invocation.source().sendMessage(Component.text("Veuillez saisir un argument."));
      return;
    }

    switch (invocation.arguments()[0].toLowerCase(Locale.ROOT)) {
      default: {
        invocation.source().sendMessage(Component.text("Argument non reconnu."));
        break;
      }

      case "addadmin":
        if (invocation.arguments().length < 2) {
          invocation.source().sendMessage(Component.text("Veuillez saisir une UUID."));
          return;
        }

        UUID addedUuid = UUID.fromString(invocation.arguments()[1]);

        invocation.source().sendMessage(Component.text("UUID " + addedUuid + " ajoutée."));
        velocityServer.getAdministratorsUuid().add(addedUuid);

        BasicDBObject addUuidQuery = new BasicDBObject("$addToSet",
            new BasicDBObject("administrators", addedUuid.toString()));
        velocityServer.getMongoDatabase().getCollection("network")
            .updateOne(new BsonDocument("_id", new BsonString("velocity")), addUuidQuery);
        break;

      case "removeadmin":
        if (invocation.arguments().length < 2) {
          invocation.source().sendMessage(Component.text("Veuillez saisir une UUID."));
          return;
        }

        UUID removedUuid = UUID.fromString(invocation.arguments()[1]);

        invocation.source().sendMessage(Component.text("UUID " + removedUuid + " retirée."));
        velocityServer.getAdministratorsUuid().remove(removedUuid);

        BasicDBObject match = new BasicDBObject("_id", "velocity");
        BasicDBObject query = new BasicDBObject("administrators", removedUuid.toString());
        velocityServer.getMongoDatabase().getCollection("network")
            .updateOne(match, new BasicDBObject("$pull", query));
        break;
    }
  }

  @Override
  public List<String> suggest(Invocation invocation) {
    if (invocation.arguments().length == 0) {
      return ImmutableList.of("addAdmin", "removeAdmin");
    } else {
      return Collections.emptyList();
    }
  }

  @Override
  public boolean hasPermission(Invocation invocation) {
    return SimpleCommand.super.hasPermission(invocation);
  }
}
