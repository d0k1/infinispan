package org.infinispan.commands.functional;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MultiHashtable;
import org.infinispan.commands.Visitor;
import org.infinispan.commands.write.AbstractDataWriteCommand;
import org.infinispan.commands.write.ValueMatcher;
import org.infinispan.commands.write.WriteCommand;
import org.infinispan.commons.api.functional.EntryView.WriteEntryView;
import org.infinispan.container.entries.MVCCEntry;
import org.infinispan.context.Flag;
import org.infinispan.context.InvocationContext;
import org.infinispan.functional.impl.EntryViews;
import org.infinispan.functional.impl.ListenerNotifier;
import org.infinispan.lifecycle.ComponentStatus;
import org.infinispan.metadata.Metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class WriteOnlyManyEntriesCommand<K, V> implements WriteCommand {

   // TODO: Sort out when all commands have been developed
   public static final byte COMMAND_ID = 48;

   private ListenerNotifier<K, V> notifier;
   private Map<? extends K, ? extends V> entries;
   private BiConsumer<V, WriteEntryView<V>> f;

   public WriteOnlyManyEntriesCommand(ListenerNotifier<K, V> notifier,
         Map<? extends K, ? extends V> entries, BiConsumer<V, WriteEntryView<V>> f) {
      this.entries = entries;
      this.f = f;
      this.notifier = notifier;
   }

   public WriteOnlyManyEntriesCommand() {
   }

   @Override
   public void setParameters(int commandId, Object[] parameters) {
      // TODO: Customise this generated block
   }

   @Override
   public boolean isReturnValueExpected() {
      return false;  // TODO: Customise this generated block
   }

   @Override
   public boolean canBlock() {
      return false;  // TODO: Customise this generated block
   }

   public Map<? extends K, ? extends V> getEntries() {
      return entries;
   }

   @Override
   public Object perform(InvocationContext ctx) throws Throwable {
      // Can't return a lazy stream here because the current code in
      // EntryWrappingInterceptor expects any changes to be done eagerly,
      // otherwise they're not applied. So, apply the function eagerly and
      // return a lazy stream of the void returns.
      List<Void> returns = new ArrayList<>(entries.size());
      for (Map.Entry<? extends K, ? extends V> entry : entries.entrySet()) {
         MVCCEntry<K, V> mvccEntry = (MVCCEntry<K, V>) ctx.lookupEntry(entry.getKey());
         f.accept(entry.getValue(), EntryViews.writeOnly(mvccEntry, notifier));
         returns.add(null);
      }
      return returns.stream();
   }

   @Override
   public byte getCommandId() {
      return 0;  // TODO: Customise this generated block
   }

   @Override
   public Object[] getParameters() {
      return new Object[0];  // TODO: Customise this generated block
   }

   @Override
   public boolean isSuccessful() {
      return true;
   }

   @Override
   public boolean isConditional() {
      return false;
   }

   @Override
   public ValueMatcher getValueMatcher() {
      return ValueMatcher.MATCH_ALWAYS;
   }

   @Override
   public void setValueMatcher(ValueMatcher valueMatcher) {
      // No-op
   }

   @Override
   public Set<Object> getAffectedKeys() {
      return null;  // TODO: Customise this generated block
   }

   @Override
   public void updateStatusFromRemoteResponse(Object remoteResponse) {
      // TODO: Customise this generated block
   }

   @Override
   public Object acceptVisitor(InvocationContext ctx, Visitor visitor) throws Throwable {
      return visitor.visitWriteOnlyManyEntriesCommand(ctx, this);
   }

   @Override
   public boolean shouldInvoke(InvocationContext ctx) {
      return false;  // TODO: Customise this generated block
   }

   @Override
   public boolean ignoreCommandOnStatus(ComponentStatus status) {
      return false;  // TODO: Customise this generated block
   }

   @Override
   public Set<Flag> getFlags() {
      return null;  // TODO: Customise this generated block
   }

   @Override
   public void setFlags(Set<Flag> flags) {
      // TODO: Customise this generated block
   }

   @Override
   public void setFlags(Flag... flags) {
      // TODO: Customise this generated block
   }

   @Override
   public boolean hasFlag(Flag flag) {
      return false;  // TODO: Customise this generated block
   }

   @Override
   public Metadata getMetadata() {
      return null;  // TODO: Customise this generated block
   }

   @Override
   public void setMetadata(Metadata metadata) {
      // TODO: Customise this generated block
   }

   @Override
   public int getTopologyId() {
      return 0;  // TODO: Customise this generated block
   }

   @Override
   public void setTopologyId(int topologyId) {
      // TODO: Customise this generated block
   }
}