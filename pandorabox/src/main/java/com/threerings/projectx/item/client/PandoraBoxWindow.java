package com.threerings.projectx.item.client;

import com.threerings.opengl.gui.e;
import com.threerings.projectx.client.aC;
import com.threerings.projectx.util.A;

public class PandoraBoxWindow extends i {
    public PandoraBoxWindow(A var1) {
        super(var1, new g.b());
    }

    protected final void b(e var1) {
        if (this.HS()) {
            var1.setVisible(false);
        } else {
            super.b(var1);
        }
    }

    @Override
    protected g a(g.b b) {
        return null;
    }

    private boolean HS() {
        return this.getClass() != PandoraBoxWindow.class;
    }

    protected void wasAdded() {
        super.wasAdded();
        com.threerings.projectx.client.aC.h(this._ctx).b(true, this.HS());
    }

    protected void wasRemoved() {
        com.threerings.projectx.client.aC var1 = aC.h(this._ctx);
        super.wasRemoved();
        if (var1 != null) {
            var1.b(false, this.HS());
        }

    }

    protected ArsenalPanel Bj() {
        if (!this.HS()) {
            X.aKy.clear();
        }

        return new ArsenalPanel(this._ctx);
    }
}
