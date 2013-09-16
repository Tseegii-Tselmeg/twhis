package cc.johnwu.loading;


public class Frm_Loading extends javax.swing.JFrame {

    private final int THREADAMOUNT = 10;
    private int s_UpdateCurrent;
    private int s_RemoveCurrent;
    private int s_StopThreadCount;
    private String m_TableName;
    private LoadingData m_LoadingData = new LoadingData();

    /** Creates new form Frm_Loading */
    public Frm_Loading(String tableName) {
        initComponents();
        m_TableName = tableName;
       
    }

    public void show_Loading() {

        setTitle(getTitle()+" "+m_TableName+" data...");
        if(!m_LoadingData.need2Update(m_TableName)){
            finalize();
            return;
        }
        //set this frame on center
        this.setLocationRelativeTo(this);
        m_LoadingData.rebuildingData(m_TableName);
        pbar_Loading.setMinimum(0);
        pbar_Loading.setValue(0);
        pbar_Loading.setStringPainted(true);
        pbar_Loading.setMaximum(m_LoadingData.getServerDataCount()+m_LoadingData.getLocalDataCount());
        for(int i=0; i<THREADAMOUNT; i++){
            new UpdateLDB(""+i).start();
        }
        setVisible(true);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pbar_Loading = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Loading");
        setAlwaysOnTop(true);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pbar_Loading, javax.swing.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pbar_Loading, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected void finalize(){
        System.gc();
        this.dispose();
    }

    private class UpdateLDB extends Thread{
        UpdateLDB(String name){
            super(name);
        }
        @Override
        public void run(){
            while(++s_UpdateCurrent <= pbar_Loading.getMaximum()-m_LoadingData.getLocalDataCount()){
                m_LoadingData.download2LocalDB(m_TableName,s_UpdateCurrent);
                pbar_Loading.setValue(s_UpdateCurrent+s_RemoveCurrent);
            }
            synchronizeStop();
            this.interrupt();
        }
    }

    private void synchronizeStop(){
        s_StopThreadCount++;
        if(s_StopThreadCount==THREADAMOUNT){
            m_LoadingData.closeLocalDB();
            new Frm_Loading(m_TableName);
            finalize();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar pbar_Loading;
    // End of variables declaration//GEN-END:variables

}
